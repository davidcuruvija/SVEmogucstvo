package com.davidcuruvija.svemogucstvo.repo

import com.davidcuruvija.svemogucstvo.data.remote.WooCommerceApi
import com.davidcuruvija.svemogucstvo.model.ProductDto
import com.davidcuruvija.svemogucstvo.model.ProductVariationDto
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api : WooCommerceApi) {
    suspend fun getProducts() : List<ProductDto> {
        return api.getProducts()
    }

    suspend fun getProductVariations(productId : Int) : List<ProductVariationDto> {
        return api.getProductVariations(productId)
    }
}