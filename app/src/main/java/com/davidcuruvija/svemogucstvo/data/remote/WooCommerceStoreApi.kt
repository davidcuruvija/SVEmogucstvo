package com.davidcuruvija.svemogucstvo.data.remote

import com.davidcuruvija.svemogucstvo.model.store.StoreCartDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WooCommerceStoreApi {

    @GET("wp-json/wc/store/v1/cart")
    suspend fun getCart(
        @retrofit2.http.Header("Cart-Token") cartToken : String? = null
    ) : Response<StoreCartDto>

    @POST("wp-json/wc/store/v1/cart/add-item")
    suspend fun addItem(
        @retrofit2.http.Header("Cart-Token") cartToken : String,
        @retrofit2.http.Header("X-WC-Store-API-Nonce") nonce : String,
        @Query("id") variationId : Int,
        @Query("quantity") quantity : Int
    ) : StoreCartDto
}