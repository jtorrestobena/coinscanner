package com.bytecoders.coinscanner.data.database

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String?): Currency? {
        return value?.let { Currency.getInstance(value) }
    }

    @TypeConverter
    fun currencyToString(currency: Currency?): String? {
        return currency?.currencyCode
    }
}
