package com.davidcuruvija.svemogucstvo.viewmodel.product

import com.davidcuruvija.svemogucstvo.model.product.ProductDto
import com.davidcuruvija.svemogucstvo.model.product.ProductVariationDto

sealed interface ProductDetailsUiState {
    data object Loading : ProductDetailsUiState

    data class Success(
        val product : ProductDto,
        val variations : List<ProductVariationDto>
    ) : ProductDetailsUiState

    data class Error(
        val message : String
    ) : ProductDetailsUiState
}