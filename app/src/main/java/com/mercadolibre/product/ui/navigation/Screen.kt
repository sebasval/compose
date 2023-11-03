package com.mercadolibre.product.ui.navigation

import com.mercadolibre.product.ui.model.ProductItemInfo
import com.mercadolibre.product.ui.model.toSafeArgsString

sealed class Screen(val route: String) {
    object Products : Screen("products")
    object Images : Screen("images/{item}") {
        fun createRoute(item: ProductItemInfo): String {
            return "images/${item.toSafeArgsString()}"
        }
    }
}