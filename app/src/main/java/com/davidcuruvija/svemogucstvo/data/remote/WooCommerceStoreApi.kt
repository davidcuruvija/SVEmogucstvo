package com.davidcuruvija.svemogucstvo.data.remote

import com.davidcuruvija.svemogucstvo.model.store.StoreCartDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WooCommerceStoreApi {

    @GET("wp-json/wc/store/v1/cart")
    suspend fun getCart() : StoreCartDto

    @POST("wp-json/wc/store/v1/cart/add-item")
    suspend fun addItem(
        @Query("id") variationId : Int,
        @Query("quantity") quantity : Int
    ) : StoreCartDto
}