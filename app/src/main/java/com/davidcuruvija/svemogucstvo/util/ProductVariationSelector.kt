package com.davidcuruvija.svemogucstvo.util

import com.davidcuruvija.svemogucstvo.model.product.ProductDto
import com.davidcuruvija.svemogucstvo.model.product.ProductVariationDto

// WooCommerce attribute names are matched case-insensitively (e.g. a global
// attribute's label may be "Size" while a custom one is typed as "size"), and
// option values sometimes arrive wrapped in escaped quotes.
fun extractAttributeOptions(
    variations: List<ProductVariationDto>,
    attributeName: String
): List<String> {
    return variations
        .flatMap { variation ->
            variation.attributes
                .filter { it.name.equals(attributeName, ignoreCase = true) }
                .map { it.option.trim('"') }
        }
        .distinct()
}

fun findMatchingVariation(
    variations: List<ProductVariationDto>,
    selectedColor: String?,
    selectedSize: String?
): ProductVariationDto? {
    return variations.firstOrNull { variation ->
        val variationColor = variation.attributes
            .firstOrNull { it.name.equals("color", ignoreCase = true) }
            ?.option
            ?.trim()
            ?.trim('"')

        val variationSize = variation.attributes
            .firstOrNull { it.name.equals("size", ignoreCase = true) }
            ?.option
            ?.trim()
            ?.trim('"')

        val cleanSelectedColor = selectedColor?.trim()?.trim('"')
        val cleanSelectedSize = selectedSize?.trim()?.trim('"')

        variationColor.equals(cleanSelectedColor, ignoreCase = true) &&
                variationSize.equals(cleanSelectedSize, ignoreCase = true)
    }
}

data class ProductDisplayPrice(
    val onSale: Boolean,
    val regularPrice: String,
    val salePrice: String,
    val price: String
)

// A variable product's selected variation can have its own sale price that
// differs from the parent product, so it takes priority when present.
fun resolveDisplayPrice(
    product: ProductDto,
    selectedVariation: ProductVariationDto?
): ProductDisplayPrice {
    return ProductDisplayPrice(
        onSale = selectedVariation?.on_sale ?: product.on_sale,
        regularPrice = selectedVariation?.regular_price?.takeIf { it.isNotBlank() }
            ?: product.regular_price,
        salePrice = selectedVariation?.sale_price?.takeIf { it.isNotBlank() }
            ?: product.sale_price,
        price = selectedVariation?.price ?: product.price
    )
}
