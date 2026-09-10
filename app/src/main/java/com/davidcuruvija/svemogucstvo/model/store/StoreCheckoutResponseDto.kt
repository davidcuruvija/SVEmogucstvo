package com.davidcuruvija.svemogucstvo.model.store

data class StoreCheckoutResponseDto(
    val order_id : Int,
    val order_key : String,
    val status : String,
    val payment_result : StorePaymentResultDto?
)

data class StorePaymentResultDto(
    val payment_status : String,
    val redirect_url : String?
)
