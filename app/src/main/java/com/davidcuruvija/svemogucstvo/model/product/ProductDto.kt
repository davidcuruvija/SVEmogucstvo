package com.davidcuruvija.svemogucstvo.model.product

data class ProductDto(
    val id : Int,
    val name : String,
    val price : String,
    val regular_price : String,
    val sale_price : String,
    val on_sale : Boolean,
    val featured : Boolean,
    val type : String,
    val description : String,
    val short_description : String,
    val sku : String,
    val categories : List<ProductCategoryDto>,
    val tags : List<ProductTagDto>,
    val attributes : List<ProductAttributeDto>,
    val images : List<ProductImageDto>,
)