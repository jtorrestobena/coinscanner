package com.bytecoders.coinscanner.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object DataUtil {

    private val gson = Gson()

    fun <T>parseJson(fileName: String): T {
        val type: Type = object : TypeToken<T>() {}.type
        val myFile = ClassLoader.getSystemResource(fileName).openStream().bufferedReader()
        return gson.fromJson(myFile, type)
    }
}