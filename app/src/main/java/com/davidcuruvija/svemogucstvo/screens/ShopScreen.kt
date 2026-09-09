package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.viewmodel.product.ProductUiState
import com.davidcuruvija.svemogucstvo.viewmodel.product.ProductViewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidcuruvija.svemogucstvo.model.product.ProductDto
import com.davidcuruvija.svemogucstvo.screens.cart.CartIcon
import com.davidcuruvija.svemogucstvo.screens.product.ProductCard
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel

@Composable
fun ShopScreen(
    onProductClick: (ProductDto) -> Unit,
    onCartClick: () -> Unit,
    cartViewModel: CartViewModel,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val itemCount by cartViewModel.itemCount.collectAsStateWithLifecycle()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        ProductUiState.Loading -> {
            Text("Loading...")
        }

        is ProductUiState.Success -> {
            Scaffold(
                topBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CartIcon(
                            itemCount = itemCount,
                            onClick = onCartClick
                        )
                    }
                }
            ) { innerPadding ->

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(innerPadding)
                ) {
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
        }

        is ProductUiState.Error -> {
            Text("Error: ${state.message}")
        }
    }
}