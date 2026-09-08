package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.graphics.RectangleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(productId : Int, viewModel : ProductDetailsViewModel = hiltViewModel()) {
    var selectedColor by remember { mutableStateOf<String?>(null) }
    var selectedSize by remember { mutableStateOf<String?>(null) }
    var quantity by remember { mutableStateOf(1) }
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
                    contentDescription = state.product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = state.product.name)
                Text(text = state.product.price)
                AndroidView(
                    factory = { context ->
                        TextView(context)
                    },
                    update = { textView ->
                        textView.text = Html.fromHtml(
                            state.product.short_description,
                            Html.FROM_HTML_MODE_LEGACY
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                ProductAttributeDropdown(
                    label = "Color",
                    options = colors,
                    selectedOption = selectedColor,
                    onOptionSelected = {
                        selectedColor = it
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProductAttributeDropdown(
                    label = "Size",
                    options = sizes,
                    selectedOption = selectedSize,
                    onOptionSelected = {
                        selectedSize = it
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton (
                            onClick = {
                                if (quantity > 1) {
                                    quantity--
                                }
                            },
                            shape = RectangleShape,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Text("-")
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .border(1.dp, Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(quantity.toString())
                        }

                        OutlinedButton(
                            onClick = {
                                quantity++
                            },
                            shape = RectangleShape,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Text("+")
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            // Cart logic will go here
                        },
                        enabled = selectedVariation != null,
                        shape = RectangleShape,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("ADD TO CART")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductAttributeDropdown(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(60.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = selectedOption ?: "Choose an option",
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(text = option)
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}