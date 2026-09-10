package com.davidcuruvija.svemogucstvo.model.store

data class StoreCartItemDto(
    val key : String,
    val id : Int,
    val name : String,
    val quantity : Int,
    val prices : StoreCartItemPricesDto,
    val images : List<StoreCartItemImageDto>,
    val variation : List<StoreCartItemVariationDto>
)