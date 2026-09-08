package com.davidcuruvija.svemogucstvo.model

data class ProductVariationDto(
    val id : Int,
    val price : String,
    val stock_status : String,
    val image : ProductImageDto?,
    val attributes : List<ProductVariationAttributeDto>
)