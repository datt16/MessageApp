package io.github.datt16.msg

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class Firestore(context: Context) {

    fun setSampleData(context: Context) {
        val toast = Toast.makeText(context, "送信完了", Toast.LENGTH_SHORT)
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val user = hashMapOf("first" to "ada")
        user["last"] = "Lovelace"
        user["bron"] = "1815"
        db.collection("users").add(user).addOnSuccessListener { toast.show() }
    }
}