package com.bytecoders.coinscanner.ui.main

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.bytecoders.coinscanner.ui.navigation.BottomNavigationView
import com.bytecoders.coinscanner.ui.navigation.NavigationGraph
import com.bytecoders.coinscanner.ui.navigation.NavigationItem
import com.bytecoders.coinscanner.ui.navigation.Navigator
import com.bytecoders.coinscanner.ui.navigation.SideNavigationRailView
import com.bytecoders.coinscanner.ui.navigation.rememberNavigationState
import com.bytecoders.coinscanner.ui.theme.AppTheme

@Preview(name = "NEXUS_7_2013", device = Devices.NEXUS_7_2013)
@Composable
fun MainScreenNexus7() {
    MainScreen(widthSizeClass = WindowWidthSizeClass.Medium)
}

@Preview(name = "NEXUS_9", device = Devices.NEXUS_9)
@Composable
fun MainScreenNexus9() {
    MainScreen(widthSizeClass = WindowWidthSizeClass.Medium)
}

@Preview(name = "NEXUS_10", device = Devices.NEXUS_10)
@Composable
fun MainScreenNexus10() {
    MainScreen(widthSizeClass = WindowWidthSizeClass.Expanded)
}

@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL)
@Composable
fun MainScreenPixel4xl() {
    MainScreen(widthSizeClass = WindowWidthSizeClass.Compact)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(widthSizeClass: WindowWidthSizeClass) {
    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
    AppTheme {
        val navigationState = rememberNavigationState(
            startRoute = NavigationItem.Home,
            topLevelRoutes = setOf(
                NavigationItem.Home,
                NavigationItem.Dashboard,
                NavigationItem.Notifications
            )
        )
        val navigator = remember { Navigator(navigationState) }

        Scaffold(
            bottomBar = {
                if (!isExpandedScreen) {
                    BottomNavigationView(
                        navigationState = navigationState,
                        navigator = navigator
                    )
                }
            }
        ) { paddingValues ->
            if (isExpandedScreen) {
                Row {
                    SideNavigationRailView(
                        navigationState = navigationState,
                        navigator = navigator
                    )
                    NavigationGraph(
                        navigationState = navigationState,
                        navigator = navigator,
                        bottomPadding = paddingValues.calculateBottomPadding()
                    )
                }
            } else {
                NavigationGraph(
                    navigationState = navigationState,
                    navigator = navigator,
                    bottomPadding = paddingValues.calculateBottomPadding()
                )
            }
        }
    }
}
