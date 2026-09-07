package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.viewmodel.ProductUiState
import com.davidcuruvija.svemogucstvo.viewmodel.ProductViewModel

@Composable
fun ProductTestScreen(viewModel : ProductViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        ProductUiState.Loading -> {
            Text("Loading...")
        }

        is ProductUiState.Success -> {
            Text("Loaded ${state.products.size} products")
        }

        is ProductUiState.Error -> {
            Text("Error: ${state.message}")
        }
    }
}