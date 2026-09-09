package com.davidcuruvija.svemogucstvo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.davidcuruvija.svemogucstvo.screens.cart.CartScreen
import com.davidcuruvija.svemogucstvo.screens.product.ProductDetailsScreen
import com.davidcuruvija.svemogucstvo.screens.ShopScreen
import com.davidcuruvija.svemogucstvo.screens.checkout.CheckoutScreen
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val cartViewModel: CartViewModel = viewModel()
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "shop"
            ) {
                composable("shop") {
                    ShopScreen(
                        onProductClick = { product ->
                            navController.navigate("product/${product.id}")
                        },
                        onCartClick = {
                            navController.navigate("cart")
                        },
                        cartViewModel = cartViewModel
                    )
                }
                composable("cart") {
                    CartScreen(
                        cartViewModel = cartViewModel,
                        onContinueShopping = {
                            navController.navigate("shop")
                        },
                        onCheckout = {
                            navController.navigate("checkout")
                        }
                    )
                }
                composable("checkout") {
                    CheckoutScreen(
                        cartViewModel = cartViewModel,
                        onReturnToCart = {
                            navController.popBackStack()
                        }
                    )
                }
                composable("product/{productId}") { backStackEntry ->
                    val productId = backStackEntry
                        .arguments
                        ?.getString("productId")
                        ?.toIntOrNull()

                    if (productId != null) {
                        ProductDetailsScreen(
                            productId = productId,
                            cartViewModel = cartViewModel,
                            onAddToCart = {
                                navController.navigate("cart")
                            }
                        )
                    }
                }
            }
        }
    }
}