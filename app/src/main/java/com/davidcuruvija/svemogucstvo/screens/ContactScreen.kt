package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.runtime.Composable
import com.davidcuruvija.svemogucstvo.screens.common.StaticPageScreen
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel

@Composable
fun ContactScreen(
    onCartClick: () -> Unit,
    onMenuClick: () -> Unit = {},
    cartViewModel: CartViewModel
) {
    StaticPageScreen(
        title = "Get In Touch",
        paragraphs = listOf(
            "Questions about an order, a custom idea, or just want to talk about " +
                "fabric and prints? Reach out - every message gets read, even if a " +
                "reply takes a day or two.",
            "Email: svemogucstvo@gmail.com",
            "Instagram: @sve.mogucstvo"
        ),
        onCartClick = onCartClick,
        onMenuClick = onMenuClick,
        cartViewModel = cartViewModel
    )
}
