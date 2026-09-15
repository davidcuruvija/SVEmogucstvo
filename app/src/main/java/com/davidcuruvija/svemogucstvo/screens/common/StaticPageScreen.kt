package com.davidcuruvija.svemogucstvo.screens.common

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel

// For content that rarely changes (About, Contact) - bundled directly in the app
// rather than fetched from the site, so it works offline and loads instantly.
// Update the text here and ship a new build when it needs to change.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaticPageScreen(
    title: String,
    paragraphs: List<String>,
    onCartClick: () -> Unit,
    onMenuClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    cartViewModel: CartViewModel,
    content: @Composable () -> Unit = {}
) {
    val itemCount by cartViewModel.itemCount.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BrandTopBar(
                cartItemCount = itemCount,
                onCartClick = onCartClick,
                onMenuClick = onMenuClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    paragraphs.forEachIndexed { index, paragraph ->
                        Text(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (index != paragraphs.lastIndex) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    content()
                }
            }

            BrandFooter(onNavigate = onNavigate)
        }
    }
}
