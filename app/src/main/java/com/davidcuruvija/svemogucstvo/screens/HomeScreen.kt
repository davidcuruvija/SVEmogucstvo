package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.R
import com.davidcuruvija.svemogucstvo.screens.common.BrandAsyncImage
import com.davidcuruvija.svemogucstvo.screens.common.BrandFooter
import com.davidcuruvija.svemogucstvo.screens.common.BrandTopBar
import com.davidcuruvija.svemogucstvo.ui.theme.Black
import com.davidcuruvija.svemogucstvo.ui.theme.White
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel
import com.davidcuruvija.svemogucstvo.viewmodel.home.HomeUiState
import com.davidcuruvija.svemogucstvo.viewmodel.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onShopClick: () -> Unit,
    onCartClick: () -> Unit,
    onMenuClick: () -> Unit = {},
    cartViewModel: CartViewModel,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val itemCount by cartViewModel.itemCount.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Images come from products marked "Featured" in WooCommerce, so the store
    // owner can rotate the drop shown here without an app update. Falls back to
    // the bundled campaign photos until any product is marked Featured.
    val featuredImages = (uiState as? HomeUiState.Success)
        ?.featuredProducts
        ?.mapNotNull { it.images.firstOrNull()?.src }
        ?: emptyList()

    val heroImageModel: Any = featuredImages.firstOrNull() ?: R.drawable.home_hero
    val galleryImageModels: List<Any> = featuredImages.ifEmpty {
        listOf(R.drawable.home_gallery_1, R.drawable.home_gallery_2, R.drawable.home_gallery_3)
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            HeroSection(
                imageModel = heroImageModel,
                onShopClick = onShopClick
            )

            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "ECHO COLLECTION EMBODIES QUIET STRENGTH AND REFINED CLARITY, A " +
                        "BALANCE OF SUBTLE POWER AND EXPRESSIVE DESIGN. THE DROP INTRODUCES " +
                        "THE RETURN OF THE ICONIC SIGNATURE BODYSUIT IN A NEW LILAC SHADE, THE " +
                        "UPGRADED EVERYDAY CBE 2.0 SWEATSHIRT WITH A FRESH CUT AND MESSAGE, AND " +
                        "THE BOLD LIMITED PANTS – A NEW PRODUCT, RELEASED IN A LIMITED SERIES, " +
                        "WITH A STRONG SILHOUETTE AND STRUCTURE THAT MAKES A STATEMENT.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "QUIET. STEADY. POWERFUL.",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onShopClick) {
                    Text(
                        text = "EXPLORE COLLECTION",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                galleryImageModels.forEach { model ->
                    BrandAsyncImage(
                        model = model,
                        contentDescription = null,
                        modifier = Modifier.size(220.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            BrandFooter()
        }
    }
}

@Composable
private fun HeroSection(imageModel: Any, onShopClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        BrandAsyncImage(
            model = imageModel,
            contentDescription = "Echo Collection",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Black.copy(alpha = 0f),
                        0.45f to Black.copy(alpha = 0.4f),
                        1f to Black.copy(alpha = 0.85f)
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Text(
                text = "2025 FW DROP",
                color = White,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "ECHO COLLECTION",
                color = White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onShopClick,
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = Black
                )
            ) {
                Text(
                    text = "SHOP NOW",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
