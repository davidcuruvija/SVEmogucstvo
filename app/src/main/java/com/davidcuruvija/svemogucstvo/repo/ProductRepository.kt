package com.davidcuruvija.svemogucstvo.repo

import com.davidcuruvija.svemogucstvo.data.remote.WooCommerceApi
import com.davidcuruvija.svemogucstvo.model.ProductDto
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api : WooCommerceApi) {
    suspend fun getProducts() : List<ProductDto> {
        return api.getProducts()
    }
}