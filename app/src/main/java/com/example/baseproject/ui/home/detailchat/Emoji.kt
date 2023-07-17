package com.example.baseproject.ui.home.detailchat

import com.example.baseproject.R

data class Emoji(
    val content: Int,
    var isClicked: Boolean = false
)

fun getEmoji(content: Int): Int {
    return when (content) {
        1 -> R.drawable.angry
        2 -> R.drawable.smile
        3 -> R.drawable.sleep
        4 -> R.drawable.heart
        5 -> R.drawable.sad
        6 -> R.drawable.cry
        7 -> R.drawable.cute
        8 -> R.drawable.congratulation
        9 -> R.drawable.happy
        else -> R.drawable.haha
    }
}