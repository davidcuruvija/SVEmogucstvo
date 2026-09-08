package com.davidcuruvija.svemogucstvo.viewmodel

import com.davidcuruvija.svemogucstvo.model.ProductDto
import com.davidcuruvija.svemogucstvo.model.ProductVariationDto

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