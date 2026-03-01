package com.bytecoders.coinscanner.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.bytecoders.coinscanner.R
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationItem(
    @param:StringRes val title: Int,
    @param:DrawableRes val icon: Int
) : NavKey {
    @Serializable
    data object Home : NavigationItem(R.string.title_home, R.drawable.ic_home)

    @Serializable
    data object Dashboard : NavigationItem(R.string.title_portfolio, R.drawable.ic_portfolio)

    @Serializable
    data object Notifications : NavigationItem(R.string.title_more, R.drawable.ic_more)

    @Serializable
    data object CurrencySelection : NavigationItem(R.string.title_home, R.drawable.ic_home)
}
