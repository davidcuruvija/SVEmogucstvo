package com.davidcuruvija.svemogucstvo.model.store

data class StoreCartDto(
    val items : List<StoreCartItemDto>,
    val totals : StoreCartTotalsDto,
    val items_count : Int
)