package com.bytecoders.coinscanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bytecoders.coinscanner.ui.dashboard.DashboardScreen
import com.bytecoders.coinscanner.ui.dashboard.DashboardViewModel
import com.bytecoders.coinscanner.ui.home.HomeScreen
import com.bytecoders.coinscanner.ui.notifications.NotificationsScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
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