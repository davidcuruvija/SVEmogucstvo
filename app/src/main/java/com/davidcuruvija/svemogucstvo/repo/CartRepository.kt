package com.davidcuruvija.svemogucstvo.repo

import android.text.Html
import android.util.Log
import com.davidcuruvija.svemogucstvo.data.remote.WooCommerceStoreApi
import com.davidcuruvija.svemogucstvo.data.session.CartSessionManager
import com.davidcuruvija.svemogucstvo.model.store.StoreCartDto
import javax.inject.Inject
import com.davidcuruvija.svemogucstvo.model.cart.CartItem
import com.davidcuruvija.svemogucstvo.model.store.StoreAddressDto
import com.davidcuruvija.svemogucstvo.model.store.StoreCartItemDto
import com.davidcuruvija.svemogucstvo.model.store.StoreCheckoutRequestDto
import com.davidcuruvija.svemogucstvo.model.store.StoreCheckoutResponseDto
import retrofit2.Response

class CartRepository @Inject constructor(private val api : WooCommerceStoreApi, private val sessionManager : CartSessionManager) {
    suspend fun getCart() : StoreCartDto {
        return unwrapCartResponse(api.getCart(sessionManager.getCartToken()))
    }

    suspend fun addItem(variationId : Int, quantity : Int) : StoreCartDto {
        ensureSession()
        return unwrapCartResponse(
            api.addItem(
                cartToken = sessionManager.getCartToken()
                    ?: throw IllegalStateException("Cart token is missing"),
                nonce = sessionManager.getNonce()
                    ?: throw IllegalStateException("Nonce is missing"),
                variationId = variationId,
                quantity = quantity
            )
        )
    }

    // WooCommerce rotates the Cart-Token/Nonce on every cart mutation, not just on
    // reads, so every response here must feed back into the session or subsequent
    // requests start using stale credentials and fail. A malformed 2xx body (seen
    // from the store under heavy/rapid request bursts) is also rejected here rather
    // than left to crash the caller with a raw null-pointer deep inside `.map`.
    private fun unwrapCartResponse(response : Response<StoreCartDto>) : StoreCartDto {
        val cartToken = response.headers()["Cart-Token"]
        val nonce = response.headers()["Nonce"] ?: response.headers()["X-WC-Store-API-Nonce"]

        sessionManager.updateSession(
            cartToken = cartToken,
            nonce = nonce
        )

        if (!response.isSuccessful) {
            val message = parseErrorMessage(response.errorBody()?.string())
            throw IllegalStateException(message ?: "Request failed with code ${response.code()}")
        }

        val body = response.body()

        @Suppress("SENSELESS_COMPARISON")
        if (body == null || body.items == null) {
            throw IllegalStateException("Received an unexpected response from the store. Please try again.")
        }

        return body
    }

    private suspend fun ensureSession() {
        if (sessionManager.getCartToken() != null && sessionManager.getNonce() != null) {
            return
        }
        getCart()
    }

    private fun StoreCartItemDto.toCartItem() : CartItem {

        val color = variation
            .firstOrNull {
                it.attribute.equals("color", ignoreCase = true)
            }
            ?.value
            ?.let {
                Html.fromHtml(
                    it,
                    Html.FROM_HTML_MODE_LEGACY
                ).toString()
            }
            ?.trim()
            ?.trim('"', '“', '”')

        val size = variation
            .firstOrNull {
                it.attribute.equals("size", ignoreCase = true)
            }
            ?.value
            ?.let {
                Html.fromHtml(
                    it,
                    Html.FROM_HTML_MODE_LEGACY
                ).toString()
            }
            ?.trim()
            ?.trim('"', '“', '”')

        return CartItem(
            key = key,
            variationId = id,
            productId = 0,
            productName = name,
            imageUrl = images.firstOrNull()?.src,
            color = color,
            size = size,
            price = prices.price,
            quantity = quantity
        )
    }

    suspend fun updateItemQuantity(key : String, quantity : Int) : StoreCartDto {
        ensureSession()
        return unwrapCartResponse(
            api.updateItem(
                cartToken = sessionManager.getCartToken()!!,
                nonce = sessionManager.getNonce()!!,
                key = key,
                quantity = quantity
            )
        )
    }

    suspend fun removeItem(key : String) : StoreCartDto {
        ensureSession()
        return unwrapCartResponse(
            api.removeItem(
                cartToken = sessionManager.getCartToken()!!,
                nonce = sessionManager.getNonce()!!,
                key = key
            )
        )
    }

    suspend fun checkout(
        billingAddress : StoreAddressDto,
        shippingAddress : StoreAddressDto,
        paymentMethod : String,
        customerNote : String
    ) : StoreCheckoutResponseDto {
        ensureSession()

        val response = api.checkout(
            cartToken = sessionManager.getCartToken()
                ?: throw IllegalStateException("Cart token is missing"),
            nonce = sessionManager.getNonce()
                ?: throw IllegalStateException("Nonce is missing"),
            request = StoreCheckoutRequestDto(
                billing_address = billingAddress,
                shipping_address = shippingAddress,
                payment_method = paymentMethod,
                customer_note = customerNote
            )
        )

        val cartToken = response.headers()["Cart-Token"]
        val nonce = response.headers()["Nonce"] ?: response.headers()["X-WC-Store-API-Nonce"]

        sessionManager.updateSession(
            cartToken = cartToken,
            nonce = nonce
        )

        if (!response.isSuccessful) {
            val message = parseErrorMessage(response.errorBody()?.string())
            throw IllegalStateException(message ?: "Checkout failed with code ${response.code()}")
        }

        return response.body()!!
    }

    private fun parseErrorMessage(rawError : String?) : String? {
        return rawError
            ?.let { runCatching { org.json.JSONObject(it).optString("message") }.getOrNull() }
            ?.takeIf { it.isNotBlank() }
    }

    suspend fun getCartItems() : List<CartItem> {
        val cart = getCart()

        cart.items.forEach {
            Log.d(
                "StoreCart",
                "WooCommerce item: id=${it.id}"
            )
        }

        return cart.items.map {
            it.toCartItem()
        }
    }
}