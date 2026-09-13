package com.davidcuruvija.svemogucstvo.util

import org.junit.Assert.assertEquals
import org.junit.Test

class PriceFormatterTest {

    @Test
    fun formatPrice_returnsEmptyStringForBlankInput() {
        assertEquals("", formatPrice(""))
    }

    @Test
    fun formatPrice_addsThousandsSeparatorAndCurrencySuffix() {
        assertEquals("11.500 рсд", formatPrice("11500"))
    }

    @Test
    fun formatPrice_formatsSmallAmountWithoutSeparator() {
        assertEquals("500 рсд", formatPrice("500"))
    }

    @Test
    fun formatPrice_returnsRawStringForNonNumericInput() {
        assertEquals("abc", formatPrice("abc"))
    }
}
