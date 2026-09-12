package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.model.product.ProductCategoryDto
import com.davidcuruvija.svemogucstvo.model.product.ProductDto
import com.davidcuruvija.svemogucstvo.screens.common.BrandFooter
import com.davidcuruvija.svemogucstvo.screens.common.BrandTopBar
import com.davidcuruvija.svemogucstvo.screens.product.ProductCard
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel
import com.davidcuruvija.svemogucstvo.viewmodel.product.ProductUiState
import com.davidcuruvija.svemogucstvo.viewmodel.product.ProductViewModel
import com.davidcuruvija.svemogucstvo.viewmodel.product.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    onProductClick: (ProductDto) -> Unit,
    onCartClick: () -> Unit,
    onMenuClick: () -> Unit = {},
    cartViewModel: CartViewModel,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val itemCount by cartViewModel.itemCount.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsStateWithLifecycle()
    val selectedSort by viewModel.selectedSort.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BrandTopBar(
                cartItemCount = itemCount,
                onCartClick = onCartClick,
                onMenuClick = onMenuClick
            )
        }
    ) { innerPadding ->
        // The filter bar (search field included) lives outside the loading/success/error
        // branch so a debounced search re-fetch never unmounts the text field mid-typing.
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ShopFilterBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = viewModel::onSearchQueryChanged,
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = viewModel::selectCategory,
                selectedSort = selectedSort,
                onSortSelected = viewModel::selectSort
            )

            when (val state = uiState) {
                ProductUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Loading...")
                    }
                }

                is ProductUiState.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.weight(1f)
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

                is ProductUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error: ${state.message}")
                    }
                }
            }
        }
    }
}

@Composable
private fun ShopFilterBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    categories: List<ProductCategoryDto>,
    selectedCategoryId: Int?,
    onCategorySelected: (Int?) -> Unit,
    selectedSort: SortOption,
    onSortSelected: (SortOption) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = { Text("Search products") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedCategoryId == null,
                onClick = { onCategorySelected(null) },
                label = { Text("ALL") }
            )

            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategoryId == category.id,
                    onClick = { onCategorySelected(category.id) },
                    label = { Text(category.name.uppercase()) }
                )
            }
        }

        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
            var expanded by remember { mutableStateOf(false) }

            TextButton(onClick = { expanded = true }) {
                Text(selectedSort.label.uppercase())
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.label) },
                        onClick = {
                            onSortSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}
