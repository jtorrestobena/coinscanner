package com.bytecoders.coinscanner.ui.extensions

import java.text.NumberFormat
import java.util.*

private const val DEFAULT_NUM_DECIMAL = 8

fun Double.asCurrency(currency: String, numDecimal: Int = DEFAULT_NUM_DECIMAL): String =
    NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = numDecimal
        this.currency = Currency.getInstance(currency.uppercase(Locale.ROOT))
    }.format(this)

fun Double.asPercentageChange(): String {
    val heading = if (this > 0.0F) {
        "↗ "
    } else if (this < 0.0F) {
        "↘ "
    } else {
        ""
    }
    val amount = "%.2f".format(this)

    return "$heading$amount%"
}
