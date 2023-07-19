package com.example.baseproject.ui.home.detailchat

import com.example.baseproject.R

data class Emoji(
    val content: Int
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
        10 -> R.drawable.haha
        11 -> R.drawable.rabbit_ask
        12 -> R.drawable.rabbit_couple
        13 -> R.drawable.rabbit_cute
        14 -> R.drawable.rabbit_excited
        15 -> R.drawable.rabbit_sit
        16 -> R.drawable.rabbit_run
        17 -> R.drawable.rabbit_jump
        18 -> R.drawable.rabbit_happy
        19 -> R.drawable.rabbit_dance
        20 -> R.drawable.rabbit_ok
        21 -> R.drawable.rabbit_haha
        22 -> R.drawable.bear_amazed
        23 -> R.drawable.bear_angry
        24 -> R.drawable.bear_cry
        25 -> R.drawable.bear_cute
        26 -> R.drawable.bear_disappointed
        27 -> R.drawable.bear_happy
        28 -> R.drawable.bear_shout
        29 -> R.drawable.bear_hug
        30 -> R.drawable.bear_love
        31 -> R.drawable.bear_miss
        32 -> R.drawable.bear_kiss
        else -> R.drawable.bear_sleep
    }
}