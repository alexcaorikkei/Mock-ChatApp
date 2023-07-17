package com.example.baseproject.domain.model

enum class MessageType {
    NONE,
    TEXT,
    PHOTO,
    EMOJI,
    DATE;
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
    val date: String,
    val type: MessageType,
    val text: String? = null,
    val photo: String? = null,
    val emoji: String? = null
) {
    constructor () : this("", "", "", MessageType.NONE, "", "", "")
}