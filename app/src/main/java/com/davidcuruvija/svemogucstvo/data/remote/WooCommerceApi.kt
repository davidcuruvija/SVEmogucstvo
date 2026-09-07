package com.davidcuruvija.svemogucstvo.data.remote

import com.davidcuruvija.svemogucstvo.model.ProductDto
import retrofit2.http.GET

interface WooCommerceApi {
    @GET("wp-json/wc/v3/products")
    suspend fun getProducts() : List<ProductDto>
}