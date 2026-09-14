package com.davidcuruvija.svemogucstvo.screens.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.davidcuruvija.svemogucstvo.ui.theme.AshGray
import com.davidcuruvija.svemogucstvo.ui.theme.Black
import com.davidcuruvija.svemogucstvo.ui.theme.White
import androidx.core.net.toUri

data class DrawerDestination(val label: String, val route: String)

val drawerDestinations = listOf(
    DrawerDestination("HOME", "home"),
    DrawerDestination("SHOP", "shop"),
    DrawerDestination("ABOUT", "about"),
    DrawerDestination("GALLERY", "gallery"),
    DrawerDestination("CONTACT", "contact")
)

private const val INSTAGRAM_USERNAME = "sve.mogucstvo"
private const val WEBSITE_URL = "https://svemogucstvo.com"

@Composable
fun AppDrawerContent(onNavigate: (String) -> Unit) {
    val context = LocalContext.current

    ModalDrawerSheet(
        drawerContainerColor = Black,
        modifier = Modifier.fillMaxHeight().width(280.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Column {
            drawerDestinations.forEach { destination ->
                Text(
                    text = destination.label,
                    color = White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .clickable { onNavigate(destination.route) }
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }

            Text(
                text = "WEBSITE",
                color = White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .clickable { openUrl(context, WEBSITE_URL) }
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )

            Text(
                text = "INSTAGRAM",
                color = White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .clickable { openInstagramProfile(context, INSTAGRAM_USERNAME) }
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = AshGray.copy(alpha = 0.3f))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "CASH ON DELIVERY",
            color = AshGray,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

private fun openInstagramProfile(context: Context, username: String) {
    val appIntent = Intent(
        Intent.ACTION_VIEW,
        "http://instagram.com/_u/$username".toUri()
    ).apply {
        setPackage("com.instagram.android")
    }

    try {
        context.startActivity(appIntent)
    } catch (e: ActivityNotFoundException) {
        openUrl(context, "https://instagram.com/$username")
    }
}

private fun openUrl(context: Context, url: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}
