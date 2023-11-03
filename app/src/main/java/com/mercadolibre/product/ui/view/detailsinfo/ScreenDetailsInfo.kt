package com.mercadolibre.product.ui.view.detailsinfo

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mercadolibre.product.R
import com.mercadolibre.product.ui.model.ProductItemInfo

@Composable
fun ScreenDetailsInfo(productItemInfo: ProductItemInfo){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.image_screen_title)) },
                backgroundColor = Color.Yellow,
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        ProductInfo(productItemInfo = productItemInfo)
    }
}

@Composable
fun ProductInfo(productItemInfo: ProductItemInfo) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = productItemInfo.title, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Condition: ${productItemInfo.condition}",
            style = MaterialTheme.typography.body2
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Price: ${productItemInfo.price} COP", style = MaterialTheme.typography.body1)
    }
}