package com.davidcuruvija.svemogucstvo.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.davidcuruvija.svemogucstvo.model.product.ProductDto
import com.davidcuruvija.svemogucstvo.screens.common.BrandAsyncImage
import com.davidcuruvija.svemogucstvo.ui.theme.Black
import com.davidcuruvija.svemogucstvo.ui.theme.White
import com.davidcuruvija.svemogucstvo.util.formatPrice
import com.davidcuruvija.svemogucstvo.util.resolveDisplayPrice

@Composable
fun ProductCard(product : ProductDto, onClick : () -> Unit) {
    val displayPrice = resolveDisplayPrice(product, null)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box {
            BrandAsyncImage(
                model = product.images.firstOrNull()?.src,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
            )

            if (displayPrice.onSale) {
                Text(
                    text = "SALE",
                    style = MaterialTheme.typography.labelSmall,
                    color = White,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(Black)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        product.categories.firstOrNull()?.let { category ->
            Text(
                text = category.name.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
        }

        Text(
            text = product.name.uppercase(),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (displayPrice.onSale && displayPrice.regularPrice.isNotBlank() && displayPrice.regularPrice != displayPrice.price) {
                Text(
                    text = formatPrice(displayPrice.regularPrice),
                    style = MaterialTheme.typography.bodySmall,
                    textDecoration = TextDecoration.LineThrough,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = formatPrice(displayPrice.price),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = formatPrice(displayPrice.price),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onClick,
            shape = RectangleShape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text(
                text = if (product.type == "variable") "SELECT OPTIONS" else "ADD TO CART",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
