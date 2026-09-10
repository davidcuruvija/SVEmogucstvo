package com.davidcuruvija.svemogucstvo.screens.cart

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.davidcuruvija.svemogucstvo.model.cart.CartItem
import com.davidcuruvija.svemogucstvo.util.formatPrice
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel

@Composable
fun CartScreen(
    cartViewModel : CartViewModel,
    onContinueShopping : () -> Unit,
    onCheckout : () -> Unit
) {
    val items by cartViewModel.items.collectAsStateWithLifecycle()
    val total by cartViewModel.total.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        cartViewModel.errorEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your cart is empty.",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onContinueShopping
                ) {
                    Text("CONTINUE SHOPPING")
                }
            }
        } else {
            items.forEach { item ->
                CartItemRow(
                    item = item,
                    cartViewModel = cartViewModel
                )

                HorizontalDivider()
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Total: ${formatPrice(total.toString())}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onCheckout
            ) {
                Text("CHECKOUT")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onContinueShopping
            ) {
                Text("CONTINUE SHOPPING")
            }
        }
    }
    }
}

@Composable
private fun CartItemRow(
    item : CartItem,
    cartViewModel : CartViewModel
) {
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

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    cartViewModel.decreaseQuantity(item.key)
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Text("-")
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .border(1.dp, Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.quantity.toString())
            }

            OutlinedButton(
                onClick = {
                    cartViewModel.increaseQuantity(item.key)
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Text("+")
            }
        }

        TextButton(
            onClick = {
                cartViewModel.removeItem(item.key)
            }
        ) {
            Text("REMOVE")
        }

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