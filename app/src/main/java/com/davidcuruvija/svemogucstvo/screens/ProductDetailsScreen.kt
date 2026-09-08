package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.davidcuruvija.svemogucstvo.viewmodel.ProductDetailsUiState
import com.davidcuruvija.svemogucstvo.viewmodel.ProductDetailsViewModel

@Composable
fun ProductDetailsScreen(productId : Int, viewModel : ProductDetailsViewModel = hiltViewModel()) {
    var selectedColor by remember { mutableStateOf<String?>(null) }
    var selectedSize by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        ProductDetailsUiState.Loading -> {
            Text("Loading...")
        }

        is ProductDetailsUiState.Success -> {
            val colors = state.variations
                .flatMap { variation ->
                    variation.attributes
                        .filter { it.name == "color" }
                        .map { it.option }
                }
                .distinct()

            val sizes = state.variations
                .flatMap { variation ->
                    variation.attributes
                        .filter { it.name == "size" }
                        .map { it.option }
                }
                .distinct()
            val selectedVariation = state.variations.firstOrNull { variation ->
                variation.attributes.any {
                    it.name == "color" && it.option == selectedColor
                } && variation.attributes.any {
                    it.name == "size" && it.option == selectedSize
                }
            }
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                AsyncImage(
                    model = state.product.images.firstOrNull()?.src,
                    contentDescription = state.product.name
                )
                Text(text = state.product.name)
                Text(text = state.product.price)
                Text(text = "Color")

                Row {
                    colors.forEach { color ->
                        Button(
                            onClick = {
                                selectedColor = color
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(text = color)
                        }
                    }
                }

                Text(text = "Size")

                Row {
                    sizes.forEach { size ->
                        Button(
                            onClick = {
                                selectedSize = size
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(text = size)
                        }
                    }
                }

                selectedVariation?.let { variation ->
                    Text(text = "Variation ID: ${variation.id}")
                    Text(text = "Price: ${variation.price}")
                    Text(text = "Stock: ${variation.stock_status}")
                }
            }
        }

        is ProductDetailsUiState.Error -> {
            Text("Error: ${state.message}")
        }
    }
}