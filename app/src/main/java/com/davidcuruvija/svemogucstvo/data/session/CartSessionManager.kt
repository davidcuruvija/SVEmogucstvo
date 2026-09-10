package com.davidcuruvija.svemogucstvo.data.session

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartSessionManager @Inject constructor() {

    private var cartToken : String? = null
    private var nonce : String? = null

    fun getCartToken() : String? {
        return cartToken
    }

    fun getNonce() : String? {
        return nonce
    }

    fun updateSession(cartToken : String?, nonce : String?) {
        if (cartToken != null) {
            this.cartToken = cartToken
        }

        if (nonce != null) {
            this.nonce = nonce
        }
    }
}