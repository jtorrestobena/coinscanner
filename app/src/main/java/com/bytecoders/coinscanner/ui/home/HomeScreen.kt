package com.bytecoders.coinscanner.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.ui.extensions.asCurrency
import com.bytecoders.coinscanner.ui.extensions.asPercentageChange
import com.bytecoders.coinscanner.ui.placeholder.LoadingShimmerEffect
import com.bytecoders.coinscanner.ui.theme.priceChangeColor
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    CoinList(
        coins = viewModel.markets,
        currency = viewModel.uiState.currency,
        isRefreshing = viewModel.uiState.isRefreshing,
        onRefresh = { viewModel.refreshMarkets() }
    )
}

private val coinColumns = GridCells.Adaptive(minSize = 300.dp)

@Composable
fun CoinList(
    coins: Flow<PagingData<MarketItem>>,
    currency: String,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val coinsItems: LazyPagingItems<MarketItem> = coins.collectAsLazyPagingItems()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh
    ) {
        LazyVerticalGrid(columns = coinColumns) {
            // Add a single item
            items(count = coinsItems.itemCount) { index ->
                val coin = coinsItems[index]
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinItem(coin: MarketItem, currency: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (coinImage, coinName, coinPrice, coinChange) = createRefs()
            val image = rememberCoilPainter(
                request = coin.image,
                fadeIn = true
            )
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .constrainAs(coinImage) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    },
                contentScale = ContentScale.Fit
            )
            Text(
                text = coin.name,
                modifier = Modifier
                    .constrainAs(coinName) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(coinImage.end, margin = 16.dp)
                        width = Dimension.fillToConstraints
                    }
            )
            Text(
                text = coin.currentPrice.asCurrency(currency),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .constrainAs(coinPrice) {
                        top.linkTo(coinName.bottom, margin = 8.dp)
                        start.linkTo(coinImage.end, margin = 16.dp)
                        end.linkTo(coinChange.start, margin = 16.dp)
                        width = Dimension.fillToConstraints
                    }
            )

            Text(
                text = coin.priceChangePercentage24h.asPercentageChange(),
                color = coin.priceChangePercentage24h.priceChangeColor(),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .constrainAs(coinChange) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(name = "NEXUS_7_2013", device = Devices.NEXUS_7_2013)
@Preview(name = "NEXUS_9", device = Devices.NEXUS_9)
@Preview(name = "NEXUS_10", device = Devices.NEXUS_10)
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
    LazyVerticalGrid(columns = coinColumns) {
        repeat(50) {
            item {
                CoinItem(
                    coin = MarketItem(
                        name = "Top Coin $it",
                        currentPrice = Random.nextDouble(),
                        priceChangePercentage24h = Random.nextDouble(-100.0, 100.0)
                    ),
                    "usd"
                )
            }
        }
    }
}
