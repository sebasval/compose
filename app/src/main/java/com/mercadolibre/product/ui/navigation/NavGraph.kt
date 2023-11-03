package com.mercadolibre.product.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mercadolibre.product.ui.model.ProductItemInfo
import com.mercadolibre.product.ui.model.toProductItemInfo
import com.mercadolibre.product.ui.view.image.ProductDetailScreen
import com.mercadolibre.product.ui.view.search.ProductScreen

@Preview
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }

    NavHost(navController, startDestination = Screen.Products.route) {
        composable(Screen.Products.route) { ProductScreen { actions.goToImagesScreen.invoke(it) } }
        composable(Screen.Images.route) { backStackEntry ->
            val itemString = backStackEntry.arguments?.getString("item")
            val productItemInfo = itemString?.toProductItemInfo()
            if (productItemInfo != null) {
                ProductDetailScreen(productItemInfo)
            }
        }
    }
}

class MainActions(private val navController: NavHostController) {

    val goToProductsScreen: () -> Unit = {
        navController.navigate(Screen.Products.route)
    }

    val goToImagesScreen: (ProductItemInfo) -> Unit = {
        navController.navigate(Screen.Images.createRoute(it))
    }
}