package com.davidcuruvija.svemogucstvo.viewmodel.product

enum class SortOption(val label : String, val orderBy : String?, val order : String?) {
    DEFAULT("Default sorting", null, null),
    POPULARITY("Popularity", "popularity", "desc"),
    LATEST("Latest", "date", "desc"),
    PRICE_LOW_TO_HIGH("Price: Low to High", "price", "asc"),
    PRICE_HIGH_TO_LOW("Price: High to Low", "price", "desc")
}
