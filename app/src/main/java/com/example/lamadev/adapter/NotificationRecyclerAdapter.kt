package com.example.lamadev.adapter

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lamadev.R
import com.example.lamadev.databinding.ItemNotificationBinding
import com.example.lamadev.pojo.Notification
import com.example.lamadev.pojo.Post

class NotificationRecyclerAdapter:RecyclerView.Adapter<NotificationRecyclerAdapter.RecyclerViewHolder>() {

    private  var  notifyList:ArrayList<Notification> = arrayListOf<Notification>()

    class RecyclerViewHolder(private val binding: ItemNotificationBinding, itemView: View): RecyclerView.ViewHolder(itemView) {
        fun insert(item:Notification){
            binding.notifyUser.text = Html.fromHtml(item.content,item.id)
            binding.date.text = item.updatedAt.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<ItemNotificationBinding>(inflater,
            R.layout.item_notification,parent,false)

        return RecyclerViewHolder(binding,binding.root)
    }

    override fun getItemCount(): Int {
        return  notifyList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
       val item = notifyList[position]
        holder.insert(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onChange(item:List<Notification>){
        notifyList.clear()
        notifyList.addAll(item)
        notifyDataSetChanged()

    }
}