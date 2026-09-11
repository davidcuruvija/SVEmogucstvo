package com.davidcuruvija.svemogucstvo.data.remote

import com.davidcuruvija.svemogucstvo.model.product.ProductDto
import com.davidcuruvija.svemogucstvo.model.product.ProductVariationDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WooCommerceApi {
    @GET("wp-json/wc/v3/products")
    suspend fun getProducts() : List<ProductDto>

    @GET("wp-json/wc/v3/products")
    suspend fun getFeaturedProducts(
        @Query("featured") featured : Boolean = true
    ) : List<ProductDto>

    @GET("wp-json/wc/v3/products/{productId}/variations")
    suspend fun getProductVariations(
        @Path("productId") productId : Int
    ) : List<ProductVariationDto>
}