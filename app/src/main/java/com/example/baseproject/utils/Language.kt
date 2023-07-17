package com.example.baseproject.utils

import com.example.baseproject.R

enum class Language {
    ENGLISH,
    VIETNAMESE;

    val code: String
        get() {
        return when (this) {
            ENGLISH -> "en"
            VIETNAMESE -> "vi"
        }
    }

    val displayId: Int
        get() {
            return when (this) {
                ENGLISH -> R.string.english
                VIETNAMESE -> R.string.vietnamese
            }
        }

    val itemId: Int
        get() {
            return when (this) {
                ENGLISH -> R.id.en
                VIETNAMESE -> R.id.vi
            }
        }

    companion object {
        fun getLanguageByCode(code: String?): Language {
            return when (code) {
                "en" -> Language.ENGLISH
                "vi" -> Language.VIETNAMESE
                else -> Language.ENGLISH
            }
        }
        fun getLanguageByItemId(itemId: Int?): Language {
            return when (itemId) {
                R.id.en -> Language.ENGLISH
                R.id.vi -> Language.VIETNAMESE
                else -> Language.ENGLISH
            }
        }
    }
}