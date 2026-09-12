package com.davidcuruvija.svemogucstvo.model.page

data class PageDto(
    val id : Int,
    val content : PageContentDto
)

data class PageContentDto(
    val rendered : String
)
