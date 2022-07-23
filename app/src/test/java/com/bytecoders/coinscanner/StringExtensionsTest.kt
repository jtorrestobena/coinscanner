package com.bytecoders.coinscanner

import org.junit.Assert.assertEquals
import org.junit.Test


internal class StringExtensionsTest {
    @Test
    fun ellipsis() {
        assertEquals("", "".ellipsis(0))
        assertEquals("a", "a".ellipsis(1))
        assertEquals("a …", "ab".ellipsis(1))
    }
}