package com.bytecoders.coinscanner.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    CoinList(coins = viewModel.markets)
}

@Composable
fun CoinList(coins: Flow<PagingData<MarketItem>>) {
    val coinsItems: LazyPagingItems<MarketItem> = coins.collectAsLazyPagingItems()
    LazyColumn {
        // Add a single item
        items(items = coinsItems) { coin ->
            coin?.let {
                CoinItem(it)
            }
        }

        coinsItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    //You can add modifier to manage load state when first time response page is loading
                }
                loadState.append is LoadState.Loading -> {
                    //You can add modifier to manage load state when next response page is loading
                }
                loadState.append is LoadState.Error -> {
                    //You can use modifier to show error message
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinItemPreview() {
    CoinItem(coin = MarketItem(name = "Bitcoin", currentPrice = 12000.0))
}

@Composable
fun CoinItem(coin: MarketItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val image = rememberCoilPainter(
            request = coin.image,
            fadeIn = true
        )
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            contentScale = ContentScale.Fit
        )
        Column {
            Text(
                text = coin.name,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth()
            )
            Text(
                text = coin.currentPrice.toString(),
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .fillMaxWidth()
            )
        }
    }

}

//@Preview(showBackground = true)
@Composable
fun CoinListPreview() {
    CoinList(
        coins = flowOf(
            PagingData.from(
                mutableListOf<MarketItem>().apply {
                    repeat(50) {
                        add(MarketItem(name = "Top Coin $it"))
                    }
                }
            )
        )
    )
}
