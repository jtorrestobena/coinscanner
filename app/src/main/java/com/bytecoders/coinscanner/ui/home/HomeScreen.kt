package com.bytecoders.coinscanner.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.bytecoders.coinscanner.R
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.state.HomeUiState
import com.bytecoders.coinscanner.ellipsis
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import com.bytecoders.coinscanner.ui.extensions.asCurrency
import com.bytecoders.coinscanner.ui.extensions.asPercentageChange
import com.bytecoders.coinscanner.ui.navigation.RouteCurrencySelection
import com.bytecoders.coinscanner.ui.placeholder.LoadingShimmerEffect
import com.bytecoders.coinscanner.ui.theme.priceChangeColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.random.Random

@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavHostController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiStateFlowLifecycleAware = remember(viewModel.uiState, lifecycleOwner) {
        viewModel.uiState.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val uiState = uiStateFlowLifecycleAware.collectAsState(initial = HomeUiState())
    if (!uiState.value.stale) {
        CoinList(
            coins = viewModel.markets,
            currency = uiState.value.currency,
            isRefreshing = uiState.value.isRefreshing,
            onSortChanged = { viewModel.changeOrder(it) },
            selectedOrder = uiState.value.marketOrdering,
            onCurrencyClicked = {
                navController.navigate(RouteCurrencySelection) {
                    launchSingleTop = true
                }
            }
        )
    }
}

private val coinColumns = GridCells.Adaptive(minSize = 300.dp)
private val coinContentPadding = PaddingValues(8.dp)
private val coinArrangement = Arrangement.spacedBy(8.dp)

const val COIN_LIST = "COIN_LIST"
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CoinList(
    coins: Flow<PagingData<MarketItem>>,
    currency: Currency,
    isRefreshing: Boolean,
    onSortChanged: (GeckoOrder) -> Unit,
    selectedOrder: GeckoOrder,
    onCurrencyClicked: () -> Unit,
) {
    val coinsItems: LazyPagingItems<MarketItem> = coins.collectAsLazyPagingItems()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { coinsItems.refresh() }
    ) {
        LazyVerticalGrid(
            columns = coinColumns,
            contentPadding = coinContentPadding,
            verticalArrangement = coinArrangement,
            horizontalArrangement = coinArrangement,
            state = rememberLazyGridState(),
            modifier = Modifier.testTag(COIN_LIST)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow {
                    item {
                        SortMenu(selectedOrder = selectedOrder, onSortChanged = {
                            onSortChanged(it)
                            coinsItems.refresh()
                        })
                    }
                    item {
                        Chip(
                            shape = MaterialTheme.shapes.small.copy(CornerSize(8.dp)),
                            onClick = onCurrencyClicked,
                            border = BorderStroke(
                                ChipDefaults.OutlinedBorderSize,
                                MaterialTheme.colorScheme.primary
                            ),
                            colors = ChipDefaults.chipColors(
                                backgroundColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_expand_more),
                                    contentDescription = stringResource(id = selectedOrder.description)
                                )
                            }
                        ) {
                            Text(currency.displayName)
                        }
                    }
                }
            }

            // List of coins
            items(count = coinsItems.itemCount, key = {
                coinsItems[it]?.id ?: it
            }) { index ->
                val coin = coinsItems[index]
                coin?.let {
                    CoinItem(it, currency.currencyCode.lowercase(), Modifier.animateItemPlacement())
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
    CoinItem(
        coin = MarketItem(
            name = "Bitcoin",
            currentPrice = 12000.0,
            marketCap = 123123100.0,
            symbol = "btc"
        ),
        "usd",
        Modifier
    )
}

const val COIN_ITEM = "COIN_ITEM"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinItem(coin: MarketItem, currency: String, modifier: Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(COIN_ITEM)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (coinImage, coinName, coinPrice, coinTicker, coinChange) = createRefs()
            val image = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(coin.image)
                    .crossfade(true)
                    .build()
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(coinName) {
                        top.linkTo(parent.top)
                        end.linkTo(coinTicker.start)
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
                        end.linkTo(coinTicker.start, margin = 16.dp)
                        width = Dimension.fillToConstraints
                    }
            )

            Text(
                text = coin.symbol.uppercase().ellipsis(4),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .constrainAs(coinTicker) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .padding(start = 8.dp)
            )

            Text(
                text = coin.priceChangePercentage24h.asPercentageChange(),
                color = coin.priceChangePercentage24h.priceChangeColor()
                    ?: MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .constrainAs(coinChange) {
                        top.linkTo(coinTicker.bottom)
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
    /*
    This is not working as of now, see ticket:
    https://issuetracker.google.com/issues/194544557?pli=1
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
    LazyVerticalGrid(
        columns = coinColumns,
        contentPadding = coinContentPadding,
        verticalArrangement = coinArrangement,
        horizontalArrangement = coinArrangement
    ) {
        repeat(50) {
            item {
                CoinItem(
                    coin = MarketItem(
                        name = "Top Coin $it that has a very long name and does not fit easily everywhere",
                        symbol = "tc${it}couldbealsolong",
                        currentPrice = Random.nextDouble(),
                        priceChangePercentage24h = Random.nextDouble(-100.0, 100.0)
                    ),
                    "usd",
                    Modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SortMenu(selectedOrder: GeckoOrder, onSortChanged: (GeckoOrder) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Chip(
            shape = MaterialTheme.shapes.small.copy(CornerSize(8.dp)),
            onClick = { expanded = true },
            border = BorderStroke(
                ChipDefaults.OutlinedBorderSize,
                MaterialTheme.colorScheme.primary
            ),
            colors = ChipDefaults.chipColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_expand_more),
                    contentDescription = stringResource(id = selectedOrder.description)
                )
            }
        ) {
            Text(stringResource(id = selectedOrder.description))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            GeckoOrder.values().forEach { geckoOrder ->
                DropdownMenuItem(
                    text = { Text(stringResource(id = geckoOrder.description)) },
                    onClick = {
                        onSortChanged(geckoOrder)
                        expanded = false
                    },
                )
            }
        }
    }
}
