package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.screens.common.BrandFooter
import com.davidcuruvija.svemogucstvo.screens.common.BrandTopBar
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel
import com.davidcuruvija.svemogucstvo.viewmodel.page.AboutUiState
import com.davidcuruvija.svemogucstvo.viewmodel.page.AboutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onCartClick: () -> Unit,
    onMenuClick: () -> Unit = {},
    cartViewModel: CartViewModel,
    viewModel: AboutViewModel = hiltViewModel()
) {
    val itemCount by cartViewModel.itemCount.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BrandTopBar(
                cartItemCount = itemCount,
                onCartClick = onCartClick,
                onMenuClick = onMenuClick
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            AboutUiState.Loading -> {
                Text(
                    text = "Loading...",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is AboutUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = state.content.title.uppercase(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        state.content.paragraphs.forEachIndexed { index, paragraph ->
                            Text(
                                text = paragraph,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            if (index != state.content.paragraphs.lastIndex) {
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    BrandFooter()
                }
            }

            is AboutUiState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
