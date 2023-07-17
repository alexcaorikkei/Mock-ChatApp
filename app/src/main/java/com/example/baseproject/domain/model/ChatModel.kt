package com.example.baseproject.domain.model

enum class MessageType {
    NONE,
    TEXT,
    PHOTO,
    EMOJI,
    DATE;

    companion object {
        fun fromInt(value: Int): MessageType {
            return when (value) {
                1 -> NONE
                2 -> TEXT
                3 -> PHOTO
                4 -> EMOJI
                else -> DATE
            }
        }
        fun fromString(value: String): MessageType {
            return when (value) {
                "NONE" -> NONE
                "TEXT" -> TEXT
                "PHOTO" -> PHOTO
                "EMOJI" -> EMOJI
                else -> DATE
            }
        }
    }

    val viewType: Int
        get() {
            return when (this) {
                NONE -> 1
                TEXT -> 2
                PHOTO -> 3
                EMOJI -> 4
                DATE -> 5
            }
        }

    val reference: String
        get() {
            return this.name.lowercase()
        }
}
data class ChatModel(
    val id: String,
    val idSender: String,
    val date: String,
    val type: MessageType,
    val text: String? = null,
    val photo: String? = null,
    val emoji: String? = null
) {
    constructor () : this("", "", "", MessageType.NONE, "", "", "")
}