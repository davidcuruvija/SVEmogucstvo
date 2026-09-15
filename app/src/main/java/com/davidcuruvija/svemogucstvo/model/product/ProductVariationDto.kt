package com.davidcuruvija.svemogucstvo.model.product

data class ProductVariationDto(
    val id : Int,
    val price : String,
    val regular_price : String = "",
    val sale_price : String = "",
    val on_sale : Boolean = false,
    val stock_status : String,
    val image : ProductImageDto?,
    val attributes : List<ProductVariationAttributeDto>
)