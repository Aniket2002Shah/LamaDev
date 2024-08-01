package com.example.lamadev.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lamadev.R
import com.example.lamadev.databinding.ItemChatBinding
import com.example.lamadev.pojo.Chat
import com.example.lamadev.pojo.Message
import com.example.lamadev.pojo.Notification
import com.example.lamadev.pojo.User
import java.util.*
import kotlin.collections.ArrayList

class ChatRecyclerAdapter(private val item:OnItemClicked,private val data:List<Message>,private val currentUser: User):RecyclerView.Adapter<ChatRecyclerAdapter.RecyclerViewHolder>()  {

    private  var  chatList:ArrayList<Chat> = arrayListOf<Chat>()

    class RecyclerViewHolder(private val binding: ItemChatBinding, itemView: View): RecyclerView.ViewHolder(itemView) {
        val notify = binding.chatNotify
        val latestMessg = binding.recentMessage
        val notifyDate = binding.date



        @SuppressLint("SuspiciousIndentation")
        fun insert(item:Chat, data:List<Message>, currentUser: User){
//            binding.username.text = Html.fromHtml(item.notify_desc,item.id)
//            binding.date.text = item.date.toString()

            if(item.isGroup){
                if(item.chat_image!=null) {
                    Glide.with(binding.profilePic.context).load(item.chat_image).circleCrop()
                        .into(binding.profilePic)
                }else
                binding.username.text = item.chat_name
            }
            else{
               item.users.forEach {
                   if (it.id != currentUser.id) {
                       binding.username.text = it.username
                       if (it.profilePicture != null) {
                           Glide.with(binding.profilePic.context).load(it.profilePicture)
                               .circleCrop()
                               .into(binding.profilePic)
                       }
                   }
               }
            }
            var message: Message? = null
            var temp: Date = item.chatOnline_At[currentUser.id]!!
            var count = 0
            data.forEach {
                if(it.chat.id==item.id){
                   if(it.createdAt!! > temp){
                       temp = it.createdAt!!
                       message = it
                   }
                    count++
                }
            }
            if(count>0 && data.isNotEmpty() && message!=null) {
                binding.recentMessage.visibility = View.VISIBLE
                binding.date.visibility = View.VISIBLE
                binding.chatNotify.visibility = View.VISIBLE

                binding.recentMessage.text = message!!.content.toString()
                binding.date.text = message!!.createdAt.toString()
                binding.chatNotify.text = count.toString()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<ItemChatBinding>(inflater,
            R.layout.item_chat,parent,false)

        val viewHolder= RecyclerViewHolder(binding,binding.root)
        binding.layoutBox.setOnClickListener {
            item.onLayoutClicked(chatList[viewHolder.adapterPosition])
            viewHolder.latestMessg.visibility = View.GONE
            viewHolder.notify.visibility = View.GONE
            viewHolder.notifyDate.visibility = View.GONE
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return  chatList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
       val item = chatList[position]
        holder.insert(item,data,currentUser)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onChange(item:List<Chat>){
        chatList.clear()
        chatList.addAll(item)
        notifyDataSetChanged()

    }


    interface OnItemClicked{
        fun onLayoutClicked(chat:Chat)
    }
}