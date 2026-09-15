package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.davidcuruvija.svemogucstvo.R
import com.davidcuruvija.svemogucstvo.screens.common.INSTAGRAM_USERNAME
import com.davidcuruvija.svemogucstvo.screens.common.StaticPageScreen
import com.davidcuruvija.svemogucstvo.screens.common.openEmail
import com.davidcuruvija.svemogucstvo.screens.common.openInstagramProfile
import com.davidcuruvija.svemogucstvo.ui.theme.Divider
import com.davidcuruvija.svemogucstvo.ui.theme.SlateGray
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel

private const val CONTACT_EMAIL = "svemogucstvo@gmail.com"

@Composable
fun ContactScreen(
    onCartClick: () -> Unit,
    onMenuClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    cartViewModel: CartViewModel
) {
    StaticPageScreen(
        title = "Get In Touch",
        paragraphs = listOf(
            "Questions about an order, a custom idea, or just want to talk about " +
                "fabric and prints? Reach out - every message gets read, even if a " +
                "reply takes a day or two."
        ),
        onCartClick = onCartClick,
        onMenuClick = onMenuClick,
        onNavigate = onNavigate,
        cartViewModel = cartViewModel
    ) {
        ContactLinks()
    }
}

@Composable
private fun ContactLinks() {
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
        ContactRow(
            icon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
            label = "EMAIL",
            value = CONTACT_EMAIL,
            onClick = { openEmail(context, CONTACT_EMAIL) }
        )

        HorizontalDivider(color = Divider)

        ContactRow(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_instagram),
                    contentDescription = null
                )
            },
            label = "INSTAGRAM",
            value = "@$INSTAGRAM_USERNAME",
            onClick = { openInstagramProfile(context, INSTAGRAM_USERNAME) }
        )
    }
}

@Composable
private fun ContactRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(10.dp),
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.background) {
                icon()
            }
        }

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = SlateGray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
