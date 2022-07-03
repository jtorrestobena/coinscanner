package com.bytecoders.coinscanner.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.bytecoders.coinscanner.R

sealed class NavigationItem(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    object Home : NavigationItem("home", R.string.title_home, R.drawable.ic_home_black_24dp)
    object Dashboard :
        NavigationItem("dashboard", R.string.title_dashboard, R.drawable.ic_dashboard_black_24dp)

    object Notifications : NavigationItem(
        "notifications",
        R.string.title_notifications,
        R.drawable.ic_notifications_black_24dp
    )
}
