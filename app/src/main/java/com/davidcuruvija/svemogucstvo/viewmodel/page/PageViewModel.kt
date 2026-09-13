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

@HiltViewModel
class PageViewModel @Inject constructor(private val repository : PageRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<PageUiState>(PageUiState.Loading)
    val uiState : StateFlow<PageUiState> = _uiState.asStateFlow()

    fun loadPage(pageId : Int) {
        viewModelScope.launch {
            _uiState.value = PageUiState.Loading
            try {
                val content = repository.getPageContent(pageId)
                _uiState.value = PageUiState.Success(content)
            } catch (e : Exception) {
                _uiState.value = PageUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }
}
