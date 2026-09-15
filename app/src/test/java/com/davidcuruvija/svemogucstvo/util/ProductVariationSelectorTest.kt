package com.davidcuruvija.svemogucstvo.util

import com.davidcuruvija.svemogucstvo.model.product.ProductDto
import com.davidcuruvija.svemogucstvo.model.product.ProductVariationAttributeDto
import com.davidcuruvija.svemogucstvo.model.product.ProductVariationDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductVariationSelectorTest {

    private fun variation(
        id: Int,
        price: String = "1000",
        regularPrice: String = "",
        salePrice: String = "",
        onSale: Boolean = false,
        attributes: List<ProductVariationAttributeDto>
    ) = ProductVariationDto(
        id = id,
        price = price,
        regular_price = regularPrice,
        sale_price = salePrice,
        on_sale = onSale,
        stock_status = "instock",
        image = null,
        attributes = attributes
    )

    private fun product(
        price: String = "1000",
        regularPrice: String = "1000",
        salePrice: String = "",
        onSale: Boolean = false
    ) = ProductDto(
        id = 1,
        name = "Test Product",
        price = price,
        regular_price = regularPrice,
        sale_price = salePrice,
        on_sale = onSale,
        featured = false,
        type = "variable",
        description = "",
        short_description = "",
        sku = "",
        categories = emptyList(),
        tags = emptyList(),
        attributes = emptyList(),
        images = emptyList()
    )

    @Test
    fun extractAttributeOptions_matchesAttributeNameCaseInsensitively() {
        val variations = listOf(
            variation(
                id = 1,
                attributes = listOf(ProductVariationAttributeDto(name = "Size", option = "Default"))
            )
        )

        assertEquals(listOf("Default"), extractAttributeOptions(variations, "size"))
    }

    @Test
    fun extractAttributeOptions_trimsWrappingQuotesFromOptionValue() {
        val variations = listOf(
            variation(
                id = 1,
                attributes = listOf(ProductVariationAttributeDto(name = "size", option = "\"M\""))
            )
        )

        assertEquals(listOf("M"), extractAttributeOptions(variations, "size"))
    }

    @Test
    fun extractAttributeOptions_returnsDistinctValuesAcrossVariations() {
        val variations = listOf(
            variation(id = 1, attributes = listOf(ProductVariationAttributeDto("size", "S"))),
            variation(id = 2, attributes = listOf(ProductVariationAttributeDto("size", "S"))),
            variation(id = 3, attributes = listOf(ProductVariationAttributeDto("size", "M")))
        )

        assertEquals(listOf("S", "M"), extractAttributeOptions(variations, "size"))
    }

    @Test
    fun extractAttributeOptions_returnsEmptyWhenAttributeDoesNotExist() {
        val variations = listOf(
            variation(id = 1, attributes = listOf(ProductVariationAttributeDto("size", "Default")))
        )

        assertEquals(emptyList<String>(), extractAttributeOptions(variations, "color"))
    }

    @Test
    fun findMatchingVariation_matchesOnSingleSizeOnlyAttribute() {
        val bag = variation(
            id = 42,
            attributes = listOf(ProductVariationAttributeDto("Size", "Default"))
        )

        val result = findMatchingVariation(
            variations = listOf(bag),
            selectedColor = null,
            selectedSize = "Default"
        )

        assertEquals(42, result?.id)
    }

    @Test
    fun findMatchingVariation_matchesColorAndSizeCaseInsensitively() {
        val sweatshirt = variation(
            id = 7,
            attributes = listOf(
                ProductVariationAttributeDto("Color", "Black"),
                ProductVariationAttributeDto("Size", "M")
            )
        )

        val result = findMatchingVariation(
            variations = listOf(sweatshirt),
            selectedColor = "black",
            selectedSize = "m"
        )

        assertEquals(7, result?.id)
    }

    @Test
    fun findMatchingVariation_returnsNullWhenNoSelectionMatches() {
        val variations = listOf(
            variation(id = 1, attributes = listOf(ProductVariationAttributeDto("Size", "S"))),
            variation(id = 2, attributes = listOf(ProductVariationAttributeDto("Size", "M")))
        )

        val result = findMatchingVariation(
            variations = variations,
            selectedColor = null,
            selectedSize = "L"
        )

        assertNull(result)
    }

    @Test
    fun findMatchingVariation_returnsNullForEmptyVariationList() {
        val result = findMatchingVariation(
            variations = emptyList(),
            selectedColor = null,
            selectedSize = null
        )

        assertNull(result)
    }

    @Test
    fun resolveDisplayPrice_usesVariationSalePriceWhenPresent() {
        val onSaleVariation = variation(
            id = 1,
            price = "5500",
            regularPrice = "6100",
            salePrice = "5500",
            onSale = true,
            attributes = emptyList()
        )

        val result = resolveDisplayPrice(product(), onSaleVariation)

        assertEquals(true, result.onSale)
        assertEquals("6100", result.regularPrice)
        assertEquals("5500", result.salePrice)
    }

    @Test
    fun resolveDisplayPrice_fallsBackToProductPriceWhenNoVariationSelected() {
        val result = resolveDisplayPrice(
            product(price = "1000", regularPrice = "1000", onSale = false),
            selectedVariation = null
        )

        assertEquals(false, result.onSale)
        assertEquals("1000", result.price)
    }

    @Test
    fun resolveDisplayPrice_trustsSelectedVariationOnSaleFlagOverProduct() {
        // Each WooCommerce variation carries its own genuine on_sale/price data
        // (e.g. one size can be discounted while another isn't), so once a
        // variation is selected its own flag wins even if the parent product
        // (whose price reflects a different variation) is on sale.
        val notOnSaleVariation = variation(id = 1, onSale = false, attributes = emptyList())

        val result = resolveDisplayPrice(
            product(regularPrice = "6100", salePrice = "5500", onSale = true),
            notOnSaleVariation
        )

        assertEquals(false, result.onSale)
    }
}
