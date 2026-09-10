package com.davidcuruvija.svemogucstvo.repo

import android.text.Html
import android.util.Log
import com.davidcuruvija.svemogucstvo.data.remote.WooCommerceStoreApi
import com.davidcuruvija.svemogucstvo.data.session.CartSessionManager
import com.davidcuruvija.svemogucstvo.model.store.StoreCartDto
import javax.inject.Inject
import com.davidcuruvija.svemogucstvo.model.cart.CartItem
import com.davidcuruvija.svemogucstvo.model.store.StoreCartItemDto

class CartRepository @Inject constructor(private val api : WooCommerceStoreApi, private val sessionManager : CartSessionManager) {
    suspend fun getCart() : StoreCartDto {
        val response = api.getCart(sessionManager.getCartToken())

        val cartToken = response.headers()["Cart-Token"]
        val nonce = response.headers()["Nonce"] ?: response.headers()["X-WC-Store-API-Nonce"]

        sessionManager.updateSession(
            cartToken = cartToken,
            nonce = nonce
        )

        return response.body()!!
    }

    suspend fun addItem(variationId : Int, quantity : Int) : StoreCartDto {
        ensureSession()
        return api.addItem(
            cartToken = sessionManager.getCartToken()
                ?: throw IllegalStateException("Cart token is missing"),
            nonce = sessionManager.getNonce()
                ?: throw IllegalStateException("Nonce is missing"),
            variationId = variationId,
            quantity = quantity
        )
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
        return api.updateItem(
            cartToken = sessionManager.getCartToken()!!,
            nonce = sessionManager.getNonce()!!,
            key = key,
            quantity = quantity
        )
    }

    suspend fun removeItem(key : String) : StoreCartDto {
        ensureSession()
        return api.removeItem(
            cartToken = sessionManager.getCartToken()!!,
            nonce = sessionManager.getNonce()!!,
            key = key
        )
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