package com.davidcuruvija.svemogucstvo.screens

import androidx.compose.runtime.Composable
import com.davidcuruvija.svemogucstvo.screens.common.StaticPageScreen
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel

@Composable
fun AboutScreen(
    onCartClick: () -> Unit,
    onMenuClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    cartViewModel: CartViewModel
) {
    StaticPageScreen(
        title = "About Us",
        paragraphs = listOf(
            "SVEmogućstvo was launched in 2019, by Marina Ćuruvija, trained not as a " +
                "fashion designer but as an interior designer, a background that still " +
                "shapes the brand’s sense of space, structure, and material honesty. " +
                "Building it gradually taught her, in her own words, that she’s allowed " +
                "to “play, try, tear everything down, and build it back up again”.",
            "“Fashion at SVEmogućstvo isn’t just an expression of style to me; it’s " +
                "more of a tool for asking questions and telling stories”, she says. " +
                "“I’d like to hold on to what has kept me going: freedom of expression " +
                "and experimentation, questioning the rules, imperfection, mistakes, " +
                "resistance to trends and capitalist tricks; though, truth be told, I " +
                "often fall for them anyway.”"
        ),
        onCartClick = onCartClick,
        onMenuClick = onMenuClick,
        onNavigate = onNavigate,
        cartViewModel = cartViewModel
    )
}
