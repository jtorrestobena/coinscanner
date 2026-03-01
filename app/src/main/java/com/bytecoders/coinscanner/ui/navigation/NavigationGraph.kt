package com.bytecoders.coinscanner.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.bytecoders.coinscanner.ui.currency.CurrencySelectionScreen
import com.bytecoders.coinscanner.ui.home.HomeScreen
import com.bytecoders.coinscanner.ui.home.HomeViewModel
import com.bytecoders.coinscanner.ui.more.MoreScreen
import com.bytecoders.coinscanner.ui.portfolio.PortfolioScreen

@Composable
fun NavigationGraph(
    navigationState: NavigationState,
    navigator: Navigator,
    bottomPadding: Dp
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val entryProvider = entryProvider<NavKey> {
        entry<NavigationItem.Home> {
            HomeScreen(homeViewModel, navigator)
        }
        entry<NavigationItem.Dashboard> {
            PortfolioScreen(hiltViewModel())
        }
        entry<NavigationItem.Notifications> {
            MoreScreen(hiltViewModel())
        }
        entry<NavigationItem.CurrencySelection> {
            CurrencySelectionScreen(homeViewModel, navigator)
        }
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = bottomPadding)
    )
}
