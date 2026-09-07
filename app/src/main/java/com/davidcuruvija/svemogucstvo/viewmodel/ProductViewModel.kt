package com.davidcuruvija.svemogucstvo.viewmodel

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
class ProductViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)

    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            try {
                val products = repository.getProducts()
                _uiState.value = ProductUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ProductUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }
}