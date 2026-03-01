package com.bytecoders.coinscanner.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

private val items = listOf(
    NavigationItem.Home,
    NavigationItem.Dashboard,
    NavigationItem.Notifications
)

@Composable
fun BottomNavigationView(navigationState: NavigationState, navigator: Navigator) {
    NavigationBar {
        val currentRoute = navigationState.topLevelRoute
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(item.title)
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.title)
                    )
                },
                alwaysShowLabel = true,
                selected = currentRoute == item,
                onClick = {
                    navigator.navigate(item)
                }
            )
        }
    }
}

@Composable
fun SideNavigationRailView(navigationState: NavigationState, navigator: Navigator) {
    NavigationRail {
        val currentRoute = navigationState.topLevelRoute
        items.forEach { item ->
            NavigationRailItem(
                modifier = Modifier.padding(top = 16.dp),
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(item.title)
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.title)
                    )
                },
                alwaysShowLabel = true,
                selected = currentRoute == item,
                onClick = {
                    navigator.navigate(item)
                }
            )
        }
    }
}
