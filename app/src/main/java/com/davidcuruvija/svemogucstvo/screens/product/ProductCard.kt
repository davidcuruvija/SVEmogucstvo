package com.davidcuruvija.svemogucstvo.screens.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.davidcuruvija.svemogucstvo.model.product.ProductDto

@Composable
fun ProductCard(product : ProductDto, onClick : () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = product.images.firstOrNull()?.src,
            contentDescription = product.name,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = product.name,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = product.price,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}