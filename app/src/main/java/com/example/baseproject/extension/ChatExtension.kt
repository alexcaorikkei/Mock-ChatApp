package com.example.baseproject.extension

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


const val SEND_TEXT = 1
const val NO_SEND_TEXT = 0
const val NO_SEND_PHOTOS = 2
const val SEND_PHOTOS = 3
const val SEND_MULTIPHOTOS = 5
const val NO_SEND_MULTIPHOTOS = 4

fun convertDpToPixel(context: Context, dp: Int): Int {
    val metrics = context.resources.displayMetrics
    val px = dp * (metrics.densityDpi / 160f)
    return px.toInt()
}

fun getTimeCurrent(): String {
    val sdf = SimpleDateFormat("EE, dd-MM-yyyy:HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}