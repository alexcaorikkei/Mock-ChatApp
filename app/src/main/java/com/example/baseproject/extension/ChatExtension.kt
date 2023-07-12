package com.example.baseproject.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


const val RECEIVE_TEXT = 0
const val SEND_TEXT = 1
const val RECEIVE_PHOTOS = 2
const val SEND_PHOTOS = 3
const val ID_RECEIVE_N = "FScnOo6CljVmxumUJK8hUmlrgrI3"
//OVC9HAzZmFPmHrfYi7IZNExg8Us2

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

var emojiList: List<String> = listOf(
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_00.png",
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_01.png",
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_02.png",
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_03.png",
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_04.png",
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_05.png",
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_06.png",
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_08.png",
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_11.png",
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_14.png",
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_18.png",
    "https://fcbk.su/_data/stickers/usagyuuun_and_nekogyuuun/usagyuuun_and_nekogyuuun_21.png",
    "https://fcbk.su/_data/stickers/ultra_usagyuuun/ultra_usagyuuun_01.png",
    "https://fcbk.su/_data/stickers/ultra_usagyuuun/ultra_usagyuuun_02.png",
    "https://fcbk.su/_data/stickers/ultra_usagyuuun/ultra_usagyuuun_11.png",
    "https://fcbk.su/_data/stickers/ultra_usagyuuun/ultra_usagyuuun_12.png",
    "https://fcbk.su/_data/stickers/ultra_usagyuuun/ultra_usagyuuun_13.png",
    "https://fcbk.su/_data/stickers/ultra_usagyuuun/ultra_usagyuuun_17.png",
    "https://fcbk.su/_data/stickers/ultra_usagyuuun/ultra_usagyuuun_18.png",
    "https://fcbk.su/_data/stickers/ultra_usagyuuun/ultra_usagyuuun_21.png",
    "https://fcbk.su/_data/stickers/sugar_cubs_in_love/sugar_cubs_in_love_13.png",
    "https://fcbk.su/_data/stickers/sugar_cubs_in_love/sugar_cubs_in_love_14.png",
    "https://fcbk.su/_data/stickers/sugar_cubs_in_love/sugar_cubs_in_love_16.png",
    "https://fcbk.su/_data/stickers/sugar_cubs_in_love/sugar_cubs_in_love_19.png",
    "https://fcbk.su/_data/stickers/sugar_cubs_in_love/sugar_cubs_in_love_20.png",
    "https://fcbk.su/_data/stickers/sugar_cubs_in_love/sugar_cubs_in_love_21.png",
)

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
