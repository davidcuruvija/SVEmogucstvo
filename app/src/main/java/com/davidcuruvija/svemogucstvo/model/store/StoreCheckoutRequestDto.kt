package com.davidcuruvija.svemogucstvo.model.store

data class StoreCheckoutRequestDto(
    val billing_address : StoreAddressDto,
    val shipping_address : StoreAddressDto,
    val payment_method : String,
    val customer_note : String = ""
)
