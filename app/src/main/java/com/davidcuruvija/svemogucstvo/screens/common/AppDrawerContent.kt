package com.davidcuruvija.svemogucstvo.screens.common

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
import androidx.compose.ui.unit.dp
import com.davidcuruvija.svemogucstvo.ui.theme.AshGray
import com.davidcuruvija.svemogucstvo.ui.theme.Black
import com.davidcuruvija.svemogucstvo.ui.theme.White

data class DrawerDestination(val label: String, val route: String)

val drawerDestinations = listOf(
    DrawerDestination("HOME", "home"),
    DrawerDestination("SHOP", "shop"),
    DrawerDestination("ABOUT", "about"),
    DrawerDestination("BLOG", "blog"),
    DrawerDestination("CONTACT", "contact")
)

@Composable
fun AppDrawerContent(onNavigate: (String) -> Unit) {
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
