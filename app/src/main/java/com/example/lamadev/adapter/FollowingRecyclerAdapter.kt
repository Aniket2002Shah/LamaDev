package com.example.lamadev.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lamadev.R
import com.example.lamadev.databinding.ItemFollowingBinding
import com.example.lamadev.pojo.FollowerDto
import com.example.lamadev.pojo.Post
import com.example.lamadev.pojo.UserRequest

class FollowingRecyclerAdapter: RecyclerView.Adapter<FollowingRecyclerAdapter.RecyclerViewHolder>() {

    private  var  userList:ArrayList<UserRequest> = arrayListOf<UserRequest>()


    class RecyclerViewHolder(private val binding: ItemFollowingBinding, itemView: View,context: Context): RecyclerView.ViewHolder(itemView) {
        val context = context
        fun insert(item:UserRequest){
            binding.progressBarFollowing.visibility = View.VISIBLE
            Glide.with(binding.followingImg.context).load(item.profilePicture).circleCrop()
                .into(binding.followingImg)
            binding.progressBarFollowing.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<ItemFollowingBinding>(inflater,
            R.layout.item_following,parent,false)

        return RecyclerViewHolder(binding,binding.root,parent.context)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
       val item = userList[position]
        holder.insert(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onChange(item:List<UserRequest>){
       userList.clear()
       userList.addAll(item)
        notifyDataSetChanged()

    }
}