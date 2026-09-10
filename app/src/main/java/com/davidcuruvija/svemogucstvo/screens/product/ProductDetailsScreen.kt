package com.davidcuruvija.svemogucstvo.screens.product

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
import com.davidcuruvija.svemogucstvo.viewmodel.product.ProductDetailsUiState
import com.davidcuruvija.svemogucstvo.viewmodel.product.ProductDetailsViewModel
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import com.davidcuruvija.svemogucstvo.model.cart.CartItem
import com.davidcuruvija.svemogucstvo.util.formatPrice
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId : Int,
    cartViewModel : CartViewModel,
    onAddToCart: () -> Unit,
    viewModel : ProductDetailsViewModel = hiltViewModel()
) {
    var selectedColor by remember { mutableStateOf<String?>(null) }
    var selectedSize by remember { mutableStateOf<String?>(null) }
    var quantity by remember { mutableStateOf(1) }
    var selectedTab by remember { mutableStateOf(0) }

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
                        .map { it.option.trim('"') }
                }
                .distinct()

            val sizes = state.variations
                .flatMap { variation ->
                    variation.attributes
                        .filter { it.name == "size" }
                        .map { it.option.trim('"') }
                }
                .distinct()

            val selectedVariation = state.variations.firstOrNull { variation ->

                val variationColor = variation.attributes
                    .firstOrNull {
                        it.name.equals("color", ignoreCase = true)
                    }
                    ?.option
                    ?.trim()
                    ?.trim('"')

                val variationSize = variation.attributes
                    .firstOrNull {
                        it.name.equals("size", ignoreCase = true)
                    }
                    ?.option
                    ?.trim()
                    ?.trim('"')

                val cleanSelectedColor = selectedColor
                    ?.trim()
                    ?.trim('"')

                val cleanSelectedSize = selectedSize
                    ?.trim()
                    ?.trim('"')

                variationColor.equals(cleanSelectedColor, ignoreCase = true) &&
                        variationSize.equals(cleanSelectedSize, ignoreCase = true)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                AsyncImage(
                    model = state.product.images.firstOrNull()?.src,
                    contentDescription = state.product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = state.product.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 8.dp
                    )
                )
                Text(
                    text = formatPrice(
                        selectedVariation?.price ?: state.product.price
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
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
                            selectedVariation?.let { variation ->
                                cartViewModel.addItem(
                                    CartItem(
                                        key = "",
                                        variationId = variation.id,
                                        productId = state.product.id,
                                        productName = state.product.name,
                                        imageUrl = variation.image?.src
                                            ?: state.product.images.firstOrNull()?.src,
                                        color = selectedColor,
                                        size = selectedSize,
                                        price = variation.price,
                                        quantity = quantity
                                    )
                                )
                                onAddToCart()
                            }
                        },
                        enabled = selectedVariation != null,
                        shape = RectangleShape,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("ADD TO CART")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider()

                Text(
                    text = "SKU: ${state.product.sku.ifEmpty { "N/A" }}",
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                HorizontalDivider()

                Text(
                    text = "Categories: ${
                        state.product.categories.joinToString(", ") { it.name }
                    }",
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                HorizontalDivider()

                Text(
                    text = "Tags: ${
                        state.product.tags.joinToString(", ") { it.name }
                    }",
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryTabRow(
                    selectedTabIndex = selectedTab
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                        },
                        text = {
                            Text("DESCRIPTION")
                        }
                    )

                    Tab(
                        selected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                        },
                        text = {
                            Text("ADDITIONAL INFORMATION")
                        }
                    )
                }

                when (selectedTab) {
                    0 -> {
                        AndroidView(
                            factory = { context ->
                                TextView(context)
                            },
                            update = { textView ->
                                textView.text = Html.fromHtml(
                                    state.product.description,
                                    Html.FROM_HTML_MODE_LEGACY
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        )
                    }

                    1 -> {
                        Column(
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            state.product.attributes.forEach { attribute ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = attribute.name.uppercase(),
                                        modifier = Modifier.weight(0.3f)
                                    )

                                    Text(
                                        text = attribute.options
                                            .map { it.trim('"') }
                                            .joinToString(", "),
                                        modifier = Modifier.weight(0.7f)
                                    )
                                }

                                HorizontalDivider()
                            }
                        }
                    }
                }

                HorizontalDivider()

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