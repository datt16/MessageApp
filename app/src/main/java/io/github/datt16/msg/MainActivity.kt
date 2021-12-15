package io.github.datt16.msg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = CardRecyclerAdapter(this)

        val repo = MessageRepository()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val msgData = listOf(Message("0","User1", "Hello, World"), Message("1","User2", "Hello, Kotlin"))
        adapter.setList(msgData)

        val sampleMsg = Message("0", "User1", "Hello, Firestore")
        repo.add(sampleMsg)
    }
}
