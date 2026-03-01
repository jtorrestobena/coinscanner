package com.bytecoders.coinscanner.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.bytecoders.coinscanner.R

sealed class NavigationItem(
    val route: String,
    @param:StringRes val title: Int,
    @param:DrawableRes val icon: Int
) {
    object Home : NavigationItem("home", R.string.title_home, R.drawable.ic_home)
    object Dashboard :
        NavigationItem("portfolio", R.string.title_portfolio, R.drawable.ic_portfolio)

    object Notifications : NavigationItem(
        "more",
        R.string.title_more,
        R.drawable.ic_more
    )
}
