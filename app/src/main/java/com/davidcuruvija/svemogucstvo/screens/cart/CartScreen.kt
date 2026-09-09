package com.davidcuruvija.svemogucstvo.screens.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.davidcuruvija.svemogucstvo.model.cart.CartItem
import com.davidcuruvija.svemogucstvo.util.formatPrice
import com.davidcuruvija.svemogucstvo.viewmodel.CartViewModel

@Composable
fun CartScreen(cartViewModel : CartViewModel) {
    val items by cartViewModel.items.collectAsStateWithLifecycle()
    val total by cartViewModel.total.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "YOUR CART",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (items.isEmpty()) {
            Text("Your cart is empty.")
        } else {
            items.forEach { item ->
                CartItemRow(item)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Total: ${formatPrice(total.toString())}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CartItemRow(item: CartItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.productName,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = item.productName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(text = "Color: ${item.color ?: "N/A"}")
        Text(text = "Size: ${item.size ?: "N/A"}")
        Text(text = "Quantity: ${item.quantity}")
        Text(text = "Price: ${formatPrice(item.price)} × ${item.quantity}")
        Text(
            text = "Total: ${
                formatPrice(
                    ((item.price.toLongOrNull() ?: 0L) * item.quantity).toString()
                )
            }"
        )
    }
}