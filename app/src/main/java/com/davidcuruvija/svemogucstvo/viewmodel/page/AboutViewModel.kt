package com.davidcuruvija.svemogucstvo.viewmodel.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidcuruvija.svemogucstvo.repo.PageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ABOUT_PAGE_ID = 95

@HiltViewModel
class AboutViewModel @Inject constructor(private val repository : PageRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AboutUiState>(AboutUiState.Loading)
    val uiState : StateFlow<AboutUiState> = _uiState.asStateFlow()

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch {
            try {
                val content = repository.getPageContent(ABOUT_PAGE_ID)
                _uiState.value = AboutUiState.Success(content)
            } catch (e : Exception) {
                _uiState.value = AboutUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }
}
