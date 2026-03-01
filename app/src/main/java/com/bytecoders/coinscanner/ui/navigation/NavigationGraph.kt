package com.bytecoders.coinscanner.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bytecoders.coinscanner.ui.currency.CurrencySelectionScreen
import com.bytecoders.coinscanner.ui.home.HomeScreen
import com.bytecoders.coinscanner.ui.home.HomeViewModel
import com.bytecoders.coinscanner.ui.more.MoreScreen
import com.bytecoders.coinscanner.ui.portfolio.PortfolioScreen

const val RouteCurrencySelection = "RouteCurrencySelection"
@Composable
fun NavigationGraph(navController: NavHostController, bottomPadding: Dp) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    NavHost(
        navController, startDestination = NavigationItem.Home.route,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = bottomPadding)
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(homeViewModel, navController)
        }
        composable(NavigationItem.Dashboard.route) {
            PortfolioScreen(hiltViewModel())
        }
        composable(NavigationItem.Notifications.route) {
            MoreScreen(hiltViewModel())
        }

        composable(RouteCurrencySelection) {
            CurrencySelectionScreen(homeViewModel, navController)
        }
    }
}
