package com.example.baseproject.extension

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.baseproject.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object Chat {
    const val RECEIVE_TEXT = 0
    const val SEND_TEXT = 1
    const val RECEIVE_PHOTOS = 2
    const val SEND_PHOTOS = 3
    const val RECEIVE_EMOJI = 4
    const val SEND_EMOJI = 5
    const val DATE = 6
}

const val MILISECONDS_IN_A_DAY = 86400000
const val KEY_ID_RECEIVER = "KEY_ID_RECEIVER"

object Noti {
    const val CHANNEL_ID_1 = "channel1"
    const val CHANNEL_NAME_1 = "channel1"
    const val TITLE = "title"
    const val MESSAGE = "message"
}

object TypeLayoutChat {
    const val ONE = "one"
    const val START = "start"
    const val BETWEEN = "between"
    const val END = "end"
}

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

fun View.setEdittextUsableWhenFullScreen() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val animator =
            ValueAnimator.ofInt(0, insets.getInsets(WindowInsetsCompat.Type.ime()).bottom)
        animator.addUpdateListener { valueAnimator ->
            v.setPadding(0, 0, 0, valueAnimator.animatedValue as? Int ?: 0)
        }
        animator.duration = 200
        animator.start()
        insets
    }
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
fun getDateRoom(date: Long): String {
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    val hourFormat: DateFormat = SimpleDateFormat("HH:mm")
    val today = Calendar.getInstance()
    today.set(
        today.get(Calendar.YEAR),
        today.get(Calendar.MONTH),
        today.get(Calendar.DAY_OF_MONTH),
        0, 0, 0)
    val messageDate = Date(date)
    return if(dateFormat.format(today.time) == dateFormat.format(messageDate)) {
        hourFormat.format(messageDate)
    } else if(today.time.time - messageDate.time < MILISECONDS_IN_A_DAY) {
        R.string.yesterday.toString()
    } else {
        dateFormat.format(messageDate)
    }
}

fun Context.getDateChat(date: Long): String {
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

fun Context.getMinuteSecond(date: Long, dateBefore: Long): String {
    val dateFormat: DateFormat = SimpleDateFormat("hh:mm")
    val messageDate = Date(date)
    return if (isMinute(date, dateBefore)) {
        resources.getString(R.string.same_minute)
    } else {
        dateFormat.format(messageDate)
    }
}

fun isMinute(date: Long, dateBefore: Long): Boolean {
    if (date - dateBefore < 60000) return true
    return false
}