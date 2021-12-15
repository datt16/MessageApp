package io.github.datt16.msg.model

data class Message(
    val id: String,
    val writerName: String,
    val content: String
)

fun Message.toMap() : Map<String, *> {
    return hashMapOf(
        "id" to this.id,
        "writerName" to this.writerName,
        "content" to this.content
    )
}