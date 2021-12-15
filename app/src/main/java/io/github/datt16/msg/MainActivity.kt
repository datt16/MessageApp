package io.github.datt16.msg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.datt16.msg.databinding.ActivityMainBinding
import io.github.datt16.msg.model.Message
import io.github.datt16.msg.model.MessageRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
