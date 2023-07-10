package com.example.baseproject.extension

import android.content.Context
import android.view.View
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
    val sdf = SimpleDateFormat("EE, dd-MM-yyyy:HH:mm", Locale.getDefault())
    return sdf.format(Date())
}
fun convertToMinuteSecond(timeInput: String): String {
    val time = SimpleDateFormat("EE, dd-MM-yyyy:HH:mm").parse(timeInput) as Date
    val minuteSecond = SimpleDateFormat("HH:mm")
    return minuteSecond.format(time)
}

fun convertToDay(timeInput: String): String {
    val time = SimpleDateFormat("EE, dd-MM-yyyy:HH:mm").parse(timeInput) as Date
    val day = SimpleDateFormat("EE")
    return day.format(time)}