package com.davidcuruvija.svemogucstvo.model.store

data class StoreCartTotalsDto(
    val total_items : String,
    val total_shipping : String?,
    val total_price : String
)