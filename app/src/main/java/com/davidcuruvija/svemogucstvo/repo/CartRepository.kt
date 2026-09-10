package com.davidcuruvija.svemogucstvo.repo

import com.davidcuruvija.svemogucstvo.data.remote.WooCommerceStoreApi
import com.davidcuruvija.svemogucstvo.model.store.StoreCartDto
import javax.inject.Inject

class CartRepository @Inject constructor(private val api : WooCommerceStoreApi) {

    suspend fun getCart() : StoreCartDto {
        return api.getCart()
    }

    suspend fun addItem(variationId : Int, quantity : Int) : StoreCartDto {
        return api.addItem(
            variationId = variationId,
            quantity = quantity
        )
    }
}