package com.davidcuruvija.svemogucstvo.viewmodel.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidcuruvija.svemogucstvo.model.product.ProductCategoryDto
import com.davidcuruvija.svemogucstvo.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<ProductCategoryDto>>(emptyList())
    val categories: StateFlow<List<ProductCategoryDto>> = _categories.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId.asStateFlow()

    private val _selectedSort = MutableStateFlow(SortOption.DEFAULT)
    val selectedSort: StateFlow<SortOption> = _selectedSort.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchDebounceJob: Job? = null

    init {
        loadCategories()
        getProducts()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _categories.value = repository.getCategories()
            } catch (e: Exception) {
                _categories.value = emptyList()
            }
        }
    }

    private fun getProducts() {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            try {
                val products = repository.getProducts(
                    categoryId = _selectedCategoryId.value,
                    orderBy = _selectedSort.value.orderBy,
                    order = _selectedSort.value.order,
                    search = _searchQuery.value.trim().ifBlank { null }
                )
                _uiState.value = ProductUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ProductUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun selectCategory(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
        getProducts()
    }

    fun selectSort(sort: SortOption) {
        _selectedSort.value = sort
        getProducts()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            delay(400)
            getProducts()
        }
    }
}
