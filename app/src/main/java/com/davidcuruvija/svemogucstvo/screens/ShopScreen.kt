package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.viewmodel.ProductUiState
import com.davidcuruvija.svemogucstvo.viewmodel.ProductViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ShopScreen(viewModel: ProductViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        ProductUiState.Loading -> {
            Text("Loading...")
        }

        is ProductUiState.Success -> {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                state.products.forEach { product ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        AsyncImage(
                            model = product.images.firstOrNull()?.src,
                            contentDescription = product.name
                        )
                        Text(text = product.name)
                        Text(text = product.price)
                    }
                }
            }
        }

        is ProductUiState.Error -> {
            Text("Error: ${state.message}")
        }
    }
}