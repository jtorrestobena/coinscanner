package com.bytecoders.coinscanner.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bytecoders.coinscanner.R
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.data.state.HomeUiState
import com.bytecoders.coinscanner.ellipsis
import com.bytecoders.coinscanner.service.coingecko.GeckoOrder
import com.bytecoders.coinscanner.ui.extensions.asCurrency
import com.bytecoders.coinscanner.ui.extensions.asPercentageChange
import com.bytecoders.coinscanner.ui.navigation.NavigationItem
import com.bytecoders.coinscanner.ui.navigation.Navigator
import com.bytecoders.coinscanner.ui.placeholder.LoadingShimmerEffect
import com.bytecoders.coinscanner.ui.theme.priceChangeColor
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.random.Random

@Composable
fun HomeScreen(viewModel: HomeViewModel, navigator: Navigator) {
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
                navigator.navigate(NavigationItem.CurrencySelection)
            }
        )
    }
}

private val coinColumns = GridCells.Adaptive(minSize = 300.dp)
private val coinContentPadding = PaddingValues(8.dp)
private val coinArrangement = Arrangement.spacedBy(8.dp)

const val COIN_LIST = "COIN_LIST"
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { coinsItems.refresh() }
    ) {
        LazyVerticalGrid(
            columns = coinColumns,
            contentPadding = coinContentPadding,
            verticalArrangement = coinArrangement,
            horizontalArrangement = coinArrangement,
            state = rememberLazyGridState(),
            modifier = Modifier.testTag(COIN_LIST).fillMaxSize()
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
                        AssistChip(
                            onClick = onCurrencyClicked,
                            label = { Text(currency.displayName) },
                            shape = MaterialTheme.shapes.small.copy(CornerSize(8.dp)),
                            border = AssistChipDefaults.assistChipBorder(
                                enabled = true,
                                borderColor = MaterialTheme.colorScheme.primary,
                                borderWidth = 1.dp
                            ),
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = Color.Transparent,
                                labelColor = MaterialTheme.colorScheme.primary,
                                leadingIconContentColor = MaterialTheme.colorScheme.primary
                            ),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_expand_more),
                                    contentDescription = stringResource(id = selectedOrder.description)
                                )
                            }
                        )
                    }
                }
            }

            // List of coins
            items(count = coinsItems.itemCount, key = {
                coinsItems[it]?.id ?: it
            }) { index ->
                val coin = coinsItems[index]
                coin?.let {
                    CoinItem(it, currency.currencyCode.lowercase(), Modifier.animateItem())
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
                        // handled by isRefreshing
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

@Composable
fun SortMenu(selectedOrder: GeckoOrder, onSortChanged: (GeckoOrder) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        AssistChip(
            onClick = { expanded = true },
            label = { Text(stringResource(id = selectedOrder.description)) },
            shape = MaterialTheme.shapes.small.copy(CornerSize(8.dp)),
            border = AssistChipDefaults.assistChipBorder(
                enabled = true,
                borderColor = MaterialTheme.colorScheme.primary,
                borderWidth = 1.dp
            ),
            colors = AssistChipDefaults.assistChipColors(
                containerColor = Color.Transparent,
                labelColor = MaterialTheme.colorScheme.primary,
                leadingIconContentColor = MaterialTheme.colorScheme.primary
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_expand_more),
                    contentDescription = stringResource(id = selectedOrder.description)
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            GeckoOrder.entries.forEach { geckoOrder ->
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
