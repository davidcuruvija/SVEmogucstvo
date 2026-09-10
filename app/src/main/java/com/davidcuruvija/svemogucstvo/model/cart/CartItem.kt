package com.davidcuruvija.svemogucstvo.model.cart

data class CartItem(
    val key : String,
    val variationId : Int,
    val productId : Int,
    val productName : String,
    val imageUrl : String?,
    val color : String?,
    val size : String?,
    val price : String,
    val quantity : Int
)