package com.bytecoders.coinscanner.ui.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.bytecoders.coinscanner.data.coingecko.MarketItem

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val exampleEntitiesFlowLifecycleAware = remember(viewModel.markets, lifecycleOwner) {
        viewModel.markets.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val coins: List<MarketItem> by exampleEntitiesFlowLifecycleAware.collectAsState(initial = emptyList())
    CoinList(coins = coins)
}

@Composable
fun CoinList(coins: List<MarketItem>) {
    LazyColumn {
        // Add a single item
        coins.forEach {
            item {
                Text(text = it.name)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinListPreview() {
    CoinList(
        coins = mutableListOf<MarketItem>().apply {
            repeat(50) {
                add(MarketItem(name = "Top Coin $it"))
            }
        }
    )
}
