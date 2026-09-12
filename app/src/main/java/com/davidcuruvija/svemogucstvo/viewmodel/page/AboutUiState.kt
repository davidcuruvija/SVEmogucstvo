package com.davidcuruvija.svemogucstvo.viewmodel.page

import com.davidcuruvija.svemogucstvo.model.page.PageContent

sealed interface AboutUiState {
    data object Loading : AboutUiState

    data class Success(
        val content : PageContent
    ) : AboutUiState

    data class Error(
        val message : String
    ) : AboutUiState
}
