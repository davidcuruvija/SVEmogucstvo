package com.davidcuruvija.svemogucstvo.repo

import com.davidcuruvija.svemogucstvo.data.remote.WooCommerceStoreApi
import com.davidcuruvija.svemogucstvo.data.session.CartSessionManager
import com.davidcuruvija.svemogucstvo.model.store.StoreCartDto
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val api : WooCommerceStoreApi,
    private val sessionManager : CartSessionManager
) {

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
}