package com.davidcuruvija.svemogucstvo.util

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: String): String {
    if (price.isBlank()) return ""
    val amount = price.toLongOrNull() ?: return price
    return "${NumberFormat.getNumberInstance(Locale.GERMANY).format(amount)} рсд"
}