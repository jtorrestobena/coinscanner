package com.bytecoders.coinscanner

fun String.ellipsis(charCount: Int): String {
    val shortened = take(charCount)
    if (shortened.length >= length) return shortened

    return "$shortened …"
}