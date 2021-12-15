package io.github.datt16.msg

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception


class MessageRepository {
    private val db: FirebaseFirestore get() = FirebaseFirestore.getInstance()

    fun add(msg: Message): Boolean {
        var res: Boolean = false
        val collection = db.collection(COLLECTION_PATH)
        val document = collection.document(msg.id)
        val data = msg.toMap()
        document.set(data).addOnSuccessListener { res = true }
            .addOnFailureListener { Exception -> Log.d("Firestore", "add failed with ", Exception) }
        return res
    }

    fun delete(msg: Message): Boolean {
        var res: Boolean = false
        val collection = db.collection(COLLECTION_PATH)
        val document = collection.document(msg.id)
        document.delete().addOnSuccessListener { res = true }
            .addOnFailureListener { Exception ->
                Log.d(
                    "Firestore",
                    "delete failed with ",
                    Exception
                )
            }
        return res
    }

    fun fetchMessage(limit: Long): List<Message> {
        val collection = db.collection(COLLECTION_PATH)
        var messages = listOf<Message>()
        collection.limit(limit).get().addOnSuccessListener { documents ->
            if (documents != null) {
                messages = documents.map { it.data }.map { it.toMsg() }
            }
        }.addOnFailureListener { Exception ->
            Log.d("Firestore", "get failed with ", Exception)
        }
        return messages
    }

    private fun Map<String, Any>.toMsg(): Message {
        val id = this["id"] as String
        val content = this["content"] as String
        val writerName = this["writerName"] as String
        return Message(id, content, writerName)
    }

    companion object {
        private const val COLLECTION_PATH = "messages"
    }
}
