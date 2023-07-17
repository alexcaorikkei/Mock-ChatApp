package com.example.baseproject.extension

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.baseproject.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object OwnerMessage {
    const val MY_MESSAGE = 1
    const val YOUR_MESSAGE = 2
}

const val RECEIVE_TEXT = 0
const val SEND_TEXT = 1

const val RECEIVE_PHOTOS = 2
const val SEND_PHOTOS = 3

const val RECEIVE_EMOJI = 4
const val SEND_EMOJI = 5

const val ID_RECEIVE_N = "OVC9HAzZmFPmHrfYi7IZNExg8Us2"//son
const val KEY_ID_RECEIVER = "KEY_ID_RECEIVER"
//FScnOo6CljVmxumUJK8hUmlrgrI3

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken,
        0)
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

fun getTimeCurrent(): String {
    val sdf = SimpleDateFormat("EE, dd-MM-yyyy:HH:mm", Locale.getDefault())
    return sdf.format(Date())
}

fun convertToMinuteSecond(timeInput: String): String {
    return timeInput.substring(16, 21)
}

fun convertToDay(timeInput: String): String {
    return timeInput.substring(0, 1)
}
