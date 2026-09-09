package com.davidcuruvija.svemogucstvo.screens.cart

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CartIcon(itemCount: Int, onClick: () -> Unit) {
    BadgedBox(
        badge = {
            if (itemCount > 0) {
                Badge {
                    Text(itemCount.toString())
                }
            }
        }
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart"
            )
        }
    }
}