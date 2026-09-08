package com.davidcuruvija.svemogucstvo.model

data class ProductDto(
    val id: Int,
    val name: String,
    val price: String,
    val description: String,
    val short_description: String,
    val images: List<ProductImageDto>
)