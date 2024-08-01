package com.example.lamadev.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lamadev.R
import com.example.lamadev.databinding.ItemSearchBinding
import com.example.lamadev.pojo.Notification
import com.example.lamadev.pojo.Search
import com.example.lamadev.pojo.Status
import com.example.lamadev.pojo.User

class SearchListRecyclerAdapter(private  val item:OnItemClick):RecyclerView.Adapter<SearchListRecyclerAdapter.RecyclerViewHolder>() {

    private  var  userList:ArrayList<User> = arrayListOf<User>()

    class RecyclerViewHolder(private val binding: ItemSearchBinding, itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("PrivateResource")
        fun insert(item:User){
               // binding.searchProfilePic.setImageResource(com.google.android.material.R.drawable.ic_clock_black_24dp)
            Glide.with(binding.searchProfilePic.context).load(item.profilePicture).circleCrop()
                .into(binding.searchProfilePic)
                binding.username.text = item.username
                binding.work.text = item.work
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<ItemSearchBinding>(inflater,
            R.layout.item_search,parent,false)
        val viewHolder = RecyclerViewHolder(binding,binding.root)
       binding.searchProfilePic.setOnClickListener {
            item.onClick(userList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return  userList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
       val item = userList[position]
        holder.insert(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onChange(item:List<User>){
        userList.clear()
        userList.addAll(item)
        notifyDataSetChanged()

    }

    interface OnItemClick{
        fun onClick(user:User)
    }
}