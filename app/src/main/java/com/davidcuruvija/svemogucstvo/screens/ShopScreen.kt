package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.davidcuruvija.svemogucstvo.R
import com.davidcuruvija.svemogucstvo.model.product.ProductDto
import com.davidcuruvija.svemogucstvo.screens.cart.CartIcon
import com.davidcuruvija.svemogucstvo.screens.product.ProductCard
import com.davidcuruvija.svemogucstvo.ui.theme.Black
import com.davidcuruvija.svemogucstvo.ui.theme.White
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel

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
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            Row(
                                modifier = Modifier
                                    .clickable(onClick = {})
                                    .padding(start = 16.dp, end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "MENU",
                                    color = White,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        },
                        title = {
                            Image(
                                painter = painterResource(R.drawable.logo_cbe),
                                contentDescription = "SVEmogucstvo",
                                modifier = Modifier
                                    .height(44.dp)
                                    .padding(vertical = 4.dp)
                            )
                        },
                        actions = {
                            CartIcon(
                                itemCount = itemCount,
                                onClick = onCartClick
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Black,
                            titleContentColor = White,
                            actionIconContentColor = White
                        )
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
                        ShopFooter()
                    }
                }
            }
        }

        is ProductUiState.Error -> {
            Text("Error: ${state.message}")
        }
    }
}

@Composable
private fun ShopFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Black)
            .padding(vertical = 32.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CASH ON DELIVERY",
            color = White,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            listOf("ABOUT", "STUDIO", "BLOG", "CONTACT").forEach { label ->
                Text(
                    text = label,
                    color = White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Copyright 2026 © CBE",
            color = White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}