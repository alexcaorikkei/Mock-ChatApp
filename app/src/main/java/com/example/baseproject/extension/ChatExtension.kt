package com.example.baseproject.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.baseproject.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

const val RECEIVE_TEXT = 0
const val SEND_TEXT = 1

const val RECEIVE_PHOTOS = 2
const val SEND_PHOTOS = 3

const val RECEIVE_EMOJI = 4
const val SEND_EMOJI = 5

const val KEY_ID_RECEIVER = "KEY_ID_RECEIVER"

const val MILISECONDS_IN_A_DAY = 86400000

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        view.windowToken,
        0
    )
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun getIdRoom(idFirst: String, idSecond: String): String {
    if (idFirst < idSecond) return idFirst + idSecond
    return idSecond + idFirst
}

fun convertDpToPixel(context: Context, dp: Int): Int {
    val metrics = context.resources.displayMetrics
    val px = dp * (metrics.densityDpi / 160f)
    return px.toInt()
}

fun Context.getDate(date: Long): String {
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    val today = Calendar.getInstance()
    today.set(
        today.get(Calendar.YEAR),
        today.get(Calendar.MONTH),
        today.get(Calendar.DAY_OF_MONTH),
        0, 0, 0
    )
    val messageDate = Date(date)
    return if (dateFormat.format(today.time) == dateFormat.format(messageDate)) {
        resources.getString(R.string.today)
    } else if (today.time.time - messageDate.time < MILISECONDS_IN_A_DAY) {
        resources.getString(R.string.yesterday)
    } else {
        dateFormat.format(messageDate)
    }
}

fun getDate(date: Long): String {
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    val today = Calendar.getInstance()
    today.set(
        today.get(Calendar.YEAR),
        today.get(Calendar.MONTH),
        today.get(Calendar.DAY_OF_MONTH),
        0, 0, 0
    )
    val messageDate = Date(date)
    return dateFormat.format(messageDate)
}

fun getMinuteSecond(date: Long): String {
    return SimpleDateFormat("hh:mm").format(Date(date))
}