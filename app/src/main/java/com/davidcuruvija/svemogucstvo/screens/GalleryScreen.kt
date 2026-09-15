package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.R
import com.davidcuruvija.svemogucstvo.model.gallery.GalleryCollection
import com.davidcuruvija.svemogucstvo.screens.common.BrandFooter
import com.davidcuruvija.svemogucstvo.screens.common.BrandTopBar
import com.davidcuruvija.svemogucstvo.ui.theme.SlateGray
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel

// Bundled locally (res/drawable) rather than fetched from the site, so the gallery
// works offline - see the two skeleton entries below for exactly what to fill in
// when adding a collection. Duplicate one entry per collection.
val galleryCollections = listOf(
    GalleryCollection(
        name = "ECHO",
        year = "2025",
        photos = listOf(
            R.drawable.echo_4,
            R.drawable.echo_3,
            R.drawable.echo_2,
            R.drawable.echo_5,
            R.drawable.echo_6,
            R.drawable.echo_7
        )
    ),
    GalleryCollection(
        name = "СВЕ/SVE SWEATSHIRTS",
        year = "2024",
        photos = listOf(
            R.drawable.sve_1,
            R.drawable.sve_3,
            R.drawable.sve_4,
            R.drawable.sve_5,
            R.drawable.sve_9,
            R.drawable.sve_10,
            R.drawable.sve_6,
            R.drawable.sve_7,
            R.drawable.sve_2
        )
    ),

    GalleryCollection(
        name = "LIMITED EDITION",
        year = "2024",
        photos = listOf(
            R.drawable.limited_1,
            R.drawable.limited_2,
            R.drawable.limited_3,
            R.drawable.limited_4,
            R.drawable.limited_5,
            R.drawable.limited_6,
            R.drawable.limited_7,
            R.drawable.limited_8,
            R.drawable.limited_9,
            R.drawable.limited_10,
            R.drawable.limited_11,
            R.drawable.limited_12,
            R.drawable.limited_13,
            R.drawable.limited_14,
            R.drawable.limited_16,
            R.drawable.limited_15
        )
    ),
    GalleryCollection(
        name = "SVE JE U TVOJIM RUKAMA",
        year = "2023",
        photos = listOf(
            R.drawable.svejeutvojimrukama_7,
            R.drawable.svejeutvojimrukama_8,
            R.drawable.svejeutvojimrukama_2,
            R.drawable.svejeutvojimrukama_6,
            R.drawable.svejeutvojimrukama_1,
            R.drawable.svejeutvojimrukama_9,
            R.drawable.svejeutvojimrukama_16,
            R.drawable.svejeutvojimrukama_10,
            R.drawable.svejeutvojimrukama_13,
            R.drawable.svejeutvojimrukama_12,
            R.drawable.svejeutvojimrukama_15,
            R.drawable.svejeutvojimrukama_3,
            R.drawable.svejeutvojimrukama_17,
            R.drawable.svejeutvojimrukama_18
        )
    ),
    GalleryCollection(
        name = "PRIJE",
        year = "2023",
        photos = listOf(
            R.drawable.prije_10,
            R.drawable.prije_11,
            R.drawable.prije_7,
            R.drawable.prije_8,
            R.drawable.prije_5,
            R.drawable.prije_9,
            R.drawable.prije_6,
            R.drawable.prije_3,
            R.drawable.prije_4,
            R.drawable.prije_1,
            R.drawable.prije_2
        )
    ),
    GalleryCollection(
        name = "TETKE",
        year = "2022",
        photos = listOf(
            R.drawable.tetke_7,
            R.drawable.tetke_8,
            R.drawable.tetke_13,
            R.drawable.tetke_15,
            R.drawable.tetke_3,
            R.drawable.tetke_1,
            R.drawable.tetke_11,
            R.drawable.tetke_14,
            R.drawable.tetke_9,
            R.drawable.tetke_6,
            R.drawable.tetke_2,
            R.drawable.tetke_4,
            R.drawable.tetke_5,
            R.drawable.tetke_12,
            R.drawable.tetke_10
        )
    ),
    GalleryCollection(
        name = "СВЕ МОЖЕШ & FAIL 2020",
        year = "2020",
        photos = listOf(
            R.drawable.sve_mozes_4,
            R.drawable.sve_mozes_1,
            R.drawable.sve_mozes_2,
            R.drawable.sve_mozes_3,
            R.drawable.fail_2020_1,
            R.drawable.fail_2020_2
        )
    ),
    GalleryCollection(
        name = "BODYSUITS WITH GLOVES",
        year = "2020",
        photos = listOf(
            R.drawable.bodysuit_2,
            R.drawable.bodysuit_11,
            R.drawable.bodysuit_10,
            R.drawable.bodysuit_13,
            R.drawable.bodysuit_12,
            R.drawable.bodysuit_5,
            R.drawable.bodysuit_8,
            R.drawable.bodysuit_6
        )
    ),
    GalleryCollection(
        name = "СРЦЕ УШУТИ / SRCE UŠUTI",
        year = "2019",
        photos = listOf(
            R.drawable.srce_usuti_6,
            R.drawable.srce_usuti_3,
            R.drawable.srce_usuti_2,
            R.drawable.srce_usuti_4,
            R.drawable.srce_usuti_5,
            R.drawable.srce_usuti_1
        )
    ),
    GalleryCollection(
        name = "МОРАЛ & НЕ МОРАМО",
        year = "2019",
        photos = listOf(
            R.drawable.moral_1,
            R.drawable.moral_2,
            R.drawable.ne_moramo_1,
            R.drawable.ne_moramo_2
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    onCartClick : () -> Unit,
    onMenuClick : () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    cartViewModel : CartViewModel
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
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "GALLERY",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(24.dp)
            )

            galleryCollections.forEach { collection ->
                CollectionSection(collection)
            }

            Spacer(modifier = Modifier.height(8.dp))

            BrandFooter(onNavigate = onNavigate)
        }
    }
}

@Composable
private fun CollectionSection(collection : GalleryCollection) {
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = collection.name.uppercase(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = collection.year,
                style = MaterialTheme.typography.bodyMedium,
                color = SlateGray
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            collection.photos.forEach { photoRes ->
                Image(
                    painter = painterResource(photoRes),
                    contentDescription = collection.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(220.dp)
                )
            }
        }
    }
}
