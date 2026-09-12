package com.davidcuruvija.svemogucstvo.repo

import com.davidcuruvija.svemogucstvo.data.remote.WooCommerceApi
import com.davidcuruvija.svemogucstvo.model.product.ProductCategoryDto
import com.davidcuruvija.svemogucstvo.model.product.ProductDto
import com.davidcuruvija.svemogucstvo.model.product.ProductVariationDto
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api : WooCommerceApi) {
    suspend fun getProducts(
        categoryId : Int? = null,
        orderBy : String? = null,
        order : String? = null
    ) : List<ProductDto> {
        return api.getProducts(
            category = categoryId,
            orderBy = orderBy,
            order = order
        )
    }

    suspend fun getFeaturedProducts() : List<ProductDto> {
        return api.getFeaturedProducts()
    }

    suspend fun getCategories() : List<ProductCategoryDto> {
        return api.getCategories()
    }

    suspend fun getProductVariations(productId : Int) : List<ProductVariationDto> {
        return api.getProductVariations(productId)
    }
}