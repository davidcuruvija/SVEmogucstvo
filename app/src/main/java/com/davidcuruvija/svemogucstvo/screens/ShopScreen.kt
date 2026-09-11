package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.model.product.ProductDto
import com.davidcuruvija.svemogucstvo.screens.common.BrandFooter
import com.davidcuruvija.svemogucstvo.screens.common.BrandTopBar
import com.davidcuruvija.svemogucstvo.screens.product.ProductCard
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel
import com.davidcuruvija.svemogucstvo.viewmodel.product.ProductUiState
import com.davidcuruvija.svemogucstvo.viewmodel.product.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
                    BrandTopBar(
                        cartItemCount = itemCount,
                        onCartClick = onCartClick
                    )
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

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        BrandFooter()
                    }
                }
            }
        }

        is ProductUiState.Error -> {
            Text("Error: ${state.message}")
        }
    }
}
