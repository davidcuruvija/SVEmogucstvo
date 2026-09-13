package com.davidcuruvija.svemogucstvo.viewmodel.page

import com.davidcuruvija.svemogucstvo.model.page.PageContent

sealed interface PageUiState {
    data object Loading : PageUiState

    data class Success(
        val content : PageContent
    ) : PageUiState

    data class Error(
        val message : String
    ) : PageUiState
}
