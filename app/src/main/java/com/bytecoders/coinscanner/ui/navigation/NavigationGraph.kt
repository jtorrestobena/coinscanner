package com.bytecoders.coinscanner.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bytecoders.coinscanner.ui.dashboard.DashboardScreen
import com.bytecoders.coinscanner.ui.home.HomeScreen
import com.bytecoders.coinscanner.ui.notifications.NotificationsScreen

@Composable
fun NavigationGraph(navController: NavHostController, bottomPadding: Dp) {
    NavHost(
        navController, startDestination = NavigationItem.Home.route,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = bottomPadding)
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(hiltViewModel())
        }
        composable(NavigationItem.Dashboard.route) {
            DashboardScreen(hiltViewModel())
        }
        composable(NavigationItem.Notifications.route) {
            NotificationsScreen(hiltViewModel())
        }
    }
}
