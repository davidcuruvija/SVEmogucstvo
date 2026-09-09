package com.davidcuruvija.svemogucstvo.model.product

data class ProductAttributeDto(
    val id : Int,
    val name : String,
    val options : List<String>,
    val variation : Boolean
)