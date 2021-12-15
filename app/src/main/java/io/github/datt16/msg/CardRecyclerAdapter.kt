package io.github.datt16.msg

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.datt16.msg.model.Message

class CardRecyclerAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<CardRecyclerAdapter.MessageViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var numberList = emptyList<Message>() // Cached copy of words

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageContentView: TextView = itemView.findViewById(R.id.info_text)
        val messageWriterView: TextView = itemView.findViewById(R.id.text_writer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = inflater.inflate(R.layout.card_item, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val current = numberList[position]
        holder.messageContentView.text = current.content
        holder.messageWriterView.text = current.writerName
    }

    // Adapter内の numberリストに代入する
    internal fun setList(msgData: List<Message>) {
        this.numberList = msgData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return numberList.size
    }
}