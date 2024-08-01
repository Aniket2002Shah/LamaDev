package com.example.lamadev.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lamadev.R
import com.example.lamadev.pojo.Message
import java.util.*
import kotlin.collections.ArrayList


class MessageListRecyclerAdapter(private val context: Context,private val itemClick:OnItemClicked,private val chatId:Int,private val userId: Int): RecyclerView.Adapter<MessageListRecyclerAdapter.RecyclerViewHolder>(){

    private val messageList: ArrayList<Message> = arrayListOf<Message>()

     var prevDateList = ArrayList<Date>()

    override fun getItemCount(): Int {
        return messageList.size
    }

    // Determines the appropriate ViewType according to the sender of the message.
    override fun getItemViewType(position: Int): Int {
        val message: Message = messageList[position]
        return if (message.user.id==userId) {
            // If the current user is the sender of the message
            VIEW_TYPE_MESSAGE_ME
        } else {
            // If some other user sent the message
            VIEW_TYPE_MESSAGE_OTHER
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View
        val viewholder: MessageListRecyclerAdapter.RecyclerViewHolder
        try {
            if (viewType == VIEW_TYPE_MESSAGE_ME) {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_me, parent, false)
                 viewholder =  RecyclerViewHolder(view)
                viewholder.container.setOnLongClickListener {
                    itemClick.onLayoutClicked(messageList[viewholder.adapterPosition])
                    true
                }
                return viewholder
            } else if (viewType == VIEW_TYPE_MESSAGE_OTHER) {
                view = LayoutInflater.from(parent.context)
                    .inflate(com.example.lamadev.R.layout.item_chat_other, parent, false)
                viewholder =  RecyclerViewHolder(view)
                return viewholder
            }
        }
        catch (e:java.lang.Exception){
            Log.d("Shingekinokyojin","MessageRecyler View Error")
            throw Exception("MessageRecyler View Error")
        }
        return RecyclerViewHolder(View(context))
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val message: Message = messageList[position]
        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_ME -> holder.meBind(message)
            VIEW_TYPE_MESSAGE_OTHER -> holder.otherBind(message)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onChange(item:List<Message>){
        messageList.clear()
        messageList.addAll(item)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun onChange(item:Message){
        messageList.add(item)
        notifyDataSetChanged()
    }

    inner class RecyclerViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.text_gchat_message)
        val timestamp: TextView = itemView.findViewById(R.id.text_gchat_timestamp)
        val container:LinearLayout = itemView.findViewById(R.id.layout_gchat_container)
        val date: TextView  = itemView.findViewById(R.id.text_gchat_date)
    //Me
        fun meBind(message: Message) {
        if(!prevDateList.contains(message.createdAt)){
            prevDateList.add((message.createdAt!!))
            date.text = message.createdAt.toString()
        }
        else{
            date.visibility = View.GONE
        }
            messageText.text = message.content
            timestamp.text= message.createdAt.toString()
        }

    ///Other

        fun otherBind(message: Message) {
            if(!prevDateList.contains(message.createdAt)){
                prevDateList.add((message.createdAt!!))
                date.text = message.createdAt.toString()
            }
            else{
                date.visibility = View.GONE
            }
            messageText.text = message.content
            timestamp.text= message.createdAt.toString()
            val profile : ImageView = itemView.findViewById(R.id.image_gchat_profile)
            Glide.with(profile.context).load(message.user.profilePicture).circleCrop().into(profile)
        }
    }

    companion object {
        private const val VIEW_TYPE_MESSAGE_ME = 1
        private const val VIEW_TYPE_MESSAGE_OTHER = 2
    }


    interface OnItemClicked {
        fun onLayoutClicked(message: Message)
    }
}