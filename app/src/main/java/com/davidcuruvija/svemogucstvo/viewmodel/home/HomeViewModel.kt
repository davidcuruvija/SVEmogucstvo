package com.davidcuruvija.svemogucstvo.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidcuruvija.svemogucstvo.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository : ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    val uiState : StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getFeaturedProducts()
    }

    private fun getFeaturedProducts() {
        viewModelScope.launch {
            try {
                val featuredProducts = repository.getFeaturedProducts()
                _uiState.value = HomeUiState.Success(featuredProducts)
            } catch (e : Exception) {
                _uiState.value = HomeUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }
}
