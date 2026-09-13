package com.davidcuruvija.svemogucstvo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.davidcuruvija.svemogucstvo.screens.cart.CartScreen
import com.davidcuruvija.svemogucstvo.screens.product.ProductDetailsScreen
import com.davidcuruvija.svemogucstvo.screens.GalleryScreen
import com.davidcuruvija.svemogucstvo.screens.HomeScreen
import com.davidcuruvija.svemogucstvo.screens.PageScreen
import com.davidcuruvija.svemogucstvo.screens.ShopScreen
import com.davidcuruvija.svemogucstvo.screens.checkout.CheckoutScreen
import com.davidcuruvija.svemogucstvo.screens.checkout.OrderConfirmationScreen
import com.davidcuruvija.svemogucstvo.screens.common.AppDrawerContent
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel
import com.davidcuruvija.svemogucstvo.ui.theme.SVEmogucstvoTheme
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SVEmogucstvoTheme {
            val cartViewModel: CartViewModel = viewModel()
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val coroutineScope = rememberCoroutineScope()

            val openDrawer: () -> Unit = {
                coroutineScope.launch { drawerState.open() }
            }

            val navigateFromDrawer: (String) -> Unit = { route ->
                coroutineScope.launch { drawerState.close() }
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    AppDrawerContent(onNavigate = navigateFromDrawer)
                }
            ) {
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(
                        onShopClick = {
                            navController.navigate("shop")
                        },
                        onCartClick = {
                            navController.navigate("cart")
                        },
                        onMenuClick = openDrawer,
                        cartViewModel = cartViewModel
                    )
                }
                composable("shop") {
                    ShopScreen(
                        onProductClick = { product ->
                            navController.navigate("product/${product.id}")
                        },
                        onCartClick = {
                            navController.navigate("cart")
                        },
                        onMenuClick = openDrawer,
                        cartViewModel = cartViewModel
                    )
                }
                composable("about") {
                    PageScreen(
                        pageId = 95,
                        onCartClick = { navController.navigate("cart") },
                        onMenuClick = openDrawer,
                        cartViewModel = cartViewModel
                    )
                }
                composable("gallery") {
                    GalleryScreen(
                        onCartClick = { navController.navigate("cart") },
                        onMenuClick = openDrawer,
                        cartViewModel = cartViewModel
                    )
                }
                composable("contact") {
                    PageScreen(
                        pageId = 93,
                        onCartClick = { navController.navigate("cart") },
                        onMenuClick = openDrawer,
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
                        },
                        onCartClick = {},
                        onMenuClick = openDrawer
                    )
                }
                composable("checkout") {
                    CheckoutScreen(
                        cartViewModel = cartViewModel,
                        onReturnToCart = {
                            navController.popBackStack()
                        },
                        onOrderPlaced = { orderId ->
                            navController.navigate("order_confirmation/$orderId") {
                                popUpTo("shop")
                            }
                        }
                    )
                }
                composable(
                    route = "order_confirmation/{orderId}",
                    arguments = listOf(navArgument("orderId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0

                    OrderConfirmationScreen(
                        orderId = orderId,
                        onContinueShopping = {
                            navController.navigate("shop") {
                                popUpTo("shop") { inclusive = true }
                            }
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
                            },
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onCartClick = {
                                navController.navigate("cart")
                            }
                        )
                    }
                }
            }
            }
            }
        }
    }
}
