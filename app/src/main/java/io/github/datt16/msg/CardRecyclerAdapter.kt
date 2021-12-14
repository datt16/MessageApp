package io.github.datt16.msg

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardRecyclerAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<CardRecyclerAdapter.MessageViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var numberList = emptyList<Int>() // Cached copy of words

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageItemView: TextView = itemView.findViewById(R.id.info_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = inflater.inflate(R.layout.card_item, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val current = numberList[position]
        holder.messageItemView.text = current.toString()
    }

    // Adapter内の numberリストに代入する
    internal fun setList(numbers: List<Int>) {
        this.numberList = numbers
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return numberList.size
    }


}