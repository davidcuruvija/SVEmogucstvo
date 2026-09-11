package com.davidcuruvija.svemogucstvo.viewmodel.home

import com.davidcuruvija.svemogucstvo.model.product.ProductDto

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val featuredProducts : List<ProductDto>
    ) : HomeUiState

    data class Error(
        val message : String
    ) : HomeUiState
}
