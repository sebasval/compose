package com.mercadolibre.product.ui.view.search

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.mercadolibre.product.R
import com.mercadolibre.product.ui.model.ProductItemInfo
import com.mercadolibre.product.ui.theme.Shapes
import com.mercadolibre.product.ui.theme.Typography
import com.mercadolibre.component.LoadingBar
import com.mercadolibre.component.SearchTextField
import com.mercadolibre.component.ShowToast
import com.mercadolibre.data.model.ProductItem
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun ProductScreen(
    productViewModel: ProductViewModel = hiltViewModel(),
    onClick: (ProductItemInfo) -> Unit
) {

    Surface(
        color = Color.Yellow,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            Text(
                text = stringResource(R.string.cars_screen_title),
                style = Typography.h4,
                modifier = Modifier.padding(24.dp),
            )

            var search by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue())
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchTextField(
                    value = search, onValueChange = {
                        search = it
                        if (it.text.isEmpty()) {
                            productViewModel.getProducts()
                        } else {
                            productViewModel.searchProducts(it.text)
                        }
                    }, hint = stringResource(R.string.cars_screen_search_hint),
                    color = MaterialTheme.colors.background
                )
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null
                )
            }
            ProductsContent(vm = productViewModel, onImageClick = onClick)
        }
    }
}

@Composable
fun ProductsContent(vm: ProductViewModel, onImageClick: (ProductItemInfo) -> Unit) {
    vm.state.value.let { state ->
        when (state) {
            is Loading -> LoadingBar()
            is ProductListUiStateReady -> state.productList?.let {
                BindList(
                    it,
                    onImageClick = onImageClick
                )
            }
            is ProductListUiStateError -> state.error?.let { ShowToast(it) }
        }
    }
}

@Composable
fun CarImage(item: ProductItem) {
    Image(
        painter = rememberAsyncImagePainter(item.thumbnail),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .padding(end = 8.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
    )
}

@Composable
fun ListItem(item: ProductItem, onClick: (ProductItem) -> Unit) {
    Card(
        shape = Shapes.large,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        modifier = Modifier
            .height(200.dp)
            .padding(8.dp)
            .clickable { onClick.invoke(item) },
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
        ) {

            CarImage(item)
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = "Price: ${item.price} COP",
                    fontWeight = FontWeight.Bold,
                    style = Typography.subtitle2,
                    fontSize = 15.sp
                )
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    style = Typography.subtitle2,
                )
                Text(
                    text = item.condition,
                    fontWeight = FontWeight.Bold,
                    style = Typography.subtitle1,
                    color = Color.Blue
                )

            }
        }
    }
}

@Composable
fun BindList(list: List<ProductItem>, onImageClick: (ProductItemInfo) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)) {
        items(
            items = list,
            itemContent = { item ->
                ListItem(item, onClick = { onClickedItem ->
                    onClickedItem.let {
                        val encodedUrl =
                            URLEncoder.encode(it.thumbnail, StandardCharsets.UTF_8.toString())

                        onImageClick.invoke(
                            ProductItemInfo(
                                encodedUrl,
                                it.title.replace("/", ""),
                                it.condition,
                                it.price.toString()
                            )
                        )
                    }
                })
            })
    }
}


