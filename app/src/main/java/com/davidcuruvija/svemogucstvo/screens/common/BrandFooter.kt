package com.davidcuruvija.svemogucstvo.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.davidcuruvija.svemogucstvo.ui.theme.Black
import com.davidcuruvija.svemogucstvo.ui.theme.White

private data class FooterLink(val label: String, val route: String)

private val footerLinks = listOf(
    FooterLink("HOME", "home"),
    FooterLink("SHOP", "shop"),
    FooterLink("ABOUT", "about"),
    FooterLink("GALLERY", "gallery"),
    FooterLink("CONTACT", "contact"),
    FooterLink("WEBSITE", "website"),
    FooterLink("INSTAGRAM", "instagram")
)

@Composable
fun BrandFooter(onNavigate: (String) -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Black)
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CASH ON DELIVERY",
            color = White,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            footerLinks.forEach { link ->
                Text(
                    text = link.label,
                    color = White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.clickable {
                        when (link.route) {
                            "website" -> openUrl(context, WEBSITE_URL)
                            "instagram" -> openInstagramProfile(context, INSTAGRAM_USERNAME)
                            else -> onNavigate(link.route)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Copyright 2026 © CBE",
            color = White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
