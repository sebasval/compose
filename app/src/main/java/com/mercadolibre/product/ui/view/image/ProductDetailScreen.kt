package com.mercadolibre.product.ui.view.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.mercadolibre.product.R
import com.mercadolibre.product.ui.model.ProductItemInfo

@Composable
fun ProductDetailScreen(productItemInfo: ProductItemInfo) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.image_screen_title)) },
                backgroundColor = Color.Yellow,
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CarImage(item = productItemInfo.thumbnailUrl)

            Spacer(modifier = Modifier.height(16.dp))

            ProductInfo(productItemInfo)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun ProductInfo(productItemInfo: ProductItemInfo) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = productItemInfo.title, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Condition: ${productItemInfo.condition}", style = MaterialTheme.typography.body2)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Price: ${productItemInfo.price} COP", style = MaterialTheme.typography.body1)
    }
}

@Composable
fun CarImage(item: String) {
    Image(
        painter = rememberAsyncImagePainter(item),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth()
            .padding(24.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
    )
}
