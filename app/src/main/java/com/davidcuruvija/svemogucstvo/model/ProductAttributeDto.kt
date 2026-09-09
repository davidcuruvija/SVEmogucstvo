package com.davidcuruvija.svemogucstvo.model

data class ProductAttributeDto(
    val id : Int,
    val name : String,
    val options : List<String>,
    val variation : Boolean
)