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
class ProductDetailsViewModel @Inject constructor(private val repository : ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)

    val uiState : StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun loadProduct(productId : Int) {
        viewModelScope.launch {
            try {
                val product = repository.getProducts()
                    .first { it.id == productId }
                val variations = repository.getProductVariations(productId)
                _uiState.value = ProductDetailsUiState.Success(
                    product = product,
                    variations = variations
                )
            } catch (e : Exception) {
                _uiState.value = ProductDetailsUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }
}