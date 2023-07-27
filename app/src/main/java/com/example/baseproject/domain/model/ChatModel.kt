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
//    val viewType: Int
//        get() {
//            return when (this) {
//                NONE -> 1
//                TEXT -> 2
//                PHOTO -> 3
//                EMOJI -> 4
//                DATE -> 5
//            }
//        }
}

data class ChatModel(
    val id: String,
    val idSender: String,
    var date: String,
    var type: MessageType,
    var text: String? = null,
    var photo: String? = null,
    var emoji: String? = null,
    var currentDate: String? = "",
    var formatDate: String? = "",
    var typeLayout: String? = ""
) {
    constructor () : this(
        "",
        "",
        "",
        type = MessageType.NONE,
        text = "",
        photo = "",
        emoji = "",
        currentDate = "",
        formatDate = "",
        typeLayout = ""
    )
}