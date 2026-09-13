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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.screens.common.BrandFooter
import com.davidcuruvija.svemogucstvo.screens.common.BrandTopBar
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel
import com.davidcuruvija.svemogucstvo.viewmodel.page.PageUiState
import com.davidcuruvija.svemogucstvo.viewmodel.page.PageViewModel

// Renders any WordPress page's content fetched live via PageRepository - used for
// About, Contact, and any future page whose copy is simple title+paragraph text
// marked class="lead" in the page builder, so the app never needs its own copy of
// content that already lives (and gets edited) on the website.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageScreen(
    pageId: Int,
    onCartClick: () -> Unit,
    onMenuClick: () -> Unit = {},
    cartViewModel: CartViewModel,
    viewModel: PageViewModel = hiltViewModel()
) {
    val itemCount by cartViewModel.itemCount.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(pageId) {
        viewModel.loadPage(pageId)
    }

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
            PageUiState.Loading -> {
                Text(
                    text = "Loading...",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is PageUiState.Success -> {
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

            is PageUiState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
