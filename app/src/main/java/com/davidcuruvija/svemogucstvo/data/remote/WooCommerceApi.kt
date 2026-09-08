package com.davidcuruvija.svemogucstvo.data.remote

import com.davidcuruvija.svemogucstvo.model.ProductDto
import com.davidcuruvija.svemogucstvo.model.ProductVariationDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WooCommerceApi {
    @GET("wp-json/wc/v3/products")
    suspend fun getProducts() : List<ProductDto>

    @GET("wp-json/wc/v3/products/{productId}/variations")
    suspend fun getProductVariations(
        @Path("productId") productId : Int
    ) : List<ProductVariationDto>
}