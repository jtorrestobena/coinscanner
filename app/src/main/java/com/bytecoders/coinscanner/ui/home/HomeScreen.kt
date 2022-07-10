package com.bytecoders.coinscanner.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
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
import com.bytecoders.coinscanner.ui.extensions.asCurrency
import com.bytecoders.coinscanner.ui.placeholder.LoadingShimmerEffect
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    CoinList(
        coins = viewModel.markets,
        currency = viewModel.uiState.currency
    )
}

@Composable
fun CoinList(coins: Flow<PagingData<MarketItem>>, currency: String) {
    val coinsItems: LazyPagingItems<MarketItem> = coins.collectAsLazyPagingItems()

    val swipeRefreshState = rememberSwipeRefreshState(false)
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { coinsItems.refresh() }
    ) {
        LazyColumn {
            // Add a single item
            items(items = coinsItems) { coin ->
                coin?.let {
                    CoinItem(it, currency)
                }
            }

            coinsItems.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        if (coinsItems.itemCount == 0) {
                            repeat(50) {
                                item {
                                    LoadingShimmerEffect()
                                }
                            }
                        } else {
                            swipeRefreshState.isRefreshing = true
                        }
                    }
                    loadState.append is LoadState.Loading -> {
                        item {
                            LoadingShimmerEffect()
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        // TODO show error message
                    }
                    loadState.refresh is LoadState.NotLoading -> {
                        swipeRefreshState.isRefreshing = false
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinItemPreview() {
    CoinItem(coin = MarketItem(name = "Bitcoin", currentPrice = 12000.0), "usd")
}

@Composable
fun CoinItem(coin: MarketItem, currency: String) {
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
                .size(50.dp),
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
                text = coin.currentPrice.asCurrency(currency),
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinListPreview() {
    /* https://issuetracker.google.com/issues/194544557?pli=1
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
    )*/
    LazyColumn {
        repeat(50) {
            item {
                CoinItem(coin = MarketItem(name = "Top Coin $it", currentPrice = Random.nextDouble()), "usd")
            }
        }
    }
}
