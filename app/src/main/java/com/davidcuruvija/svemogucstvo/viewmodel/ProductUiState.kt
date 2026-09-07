package com.davidcuruvija.svemogucstvo.viewmodel

import com.davidcuruvija.svemogucstvo.model.ProductDto

sealed interface ProductUiState {

    data object Loading : ProductUiState

    data class Success(
        val products : List<ProductDto>
    ) : ProductUiState

    data class Error(
        val message : String
    ) : ProductUiState
}