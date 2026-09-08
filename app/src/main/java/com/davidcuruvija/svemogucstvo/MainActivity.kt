package com.davidcuruvija.svemogucstvo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.davidcuruvija.svemogucstvo.screens.ProductDetailsScreen
import com.davidcuruvija.svemogucstvo.screens.ShopScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "shop"
            ) {
                composable("shop") {
                    ShopScreen(
                        onProductClick = { product ->
                            navController.navigate("product/${product.id}")
                        }
                    )
                }
                composable("product/{productId}") { backStackEntry ->
                    val productId = backStackEntry
                        .arguments
                        ?.getString("productId")
                        ?.toIntOrNull()

                    if (productId != null) {
                        ProductDetailsScreen(productId = productId)
                    }
                }
            }
        }
    }
}