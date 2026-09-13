package com.davidcuruvija.svemogucstvo.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.davidcuruvija.svemogucstvo.R
import com.davidcuruvija.svemogucstvo.screens.cart.CartIcon
import com.davidcuruvija.svemogucstvo.ui.theme.Black
import com.davidcuruvija.svemogucstvo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandTopBar(
    cartItemCount: Int,
    onCartClick: () -> Unit,
    onMenuClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            // No drawer/menu destinations exist yet - placeholder to match the site's header.
            Row(
                modifier = Modifier
                    .clickable(onClick = onMenuClick)
                    .padding(start = 16.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "MENU",
                    color = White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        title = {
            Image(
                painter = painterResource(R.drawable.logo_sve),
                contentDescription = "SVEmogucstvo",
                modifier = Modifier
                    .height(44.dp)
                    .padding(vertical = 4.dp)
            )
        },
        actions = {
            CartIcon(
                itemCount = cartItemCount,
                onClick = onCartClick
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Black,
            scrolledContainerColor = Color.Unspecified,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = White,
            actionIconContentColor = White
        )
    )
}
