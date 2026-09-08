package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.viewmodel.ProductUiState
import com.davidcuruvija.svemogucstvo.viewmodel.ProductViewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import com.davidcuruvija.svemogucstvo.model.ProductDto

@Composable
fun ShopScreen(onProductClick: (ProductDto) -> Unit, viewModel : ProductViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        ProductUiState.Loading -> {
            Text("Loading...")
        }

        is ProductUiState.Success -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(state.products) { product ->
                    ProductCard(
                        product = product,
                        onClick = {
                            onProductClick(product)
                        }
                    )
                }
            }
        }

        is ProductUiState.Error -> {
            Text("Error: ${state.message}")
        }
    }
}