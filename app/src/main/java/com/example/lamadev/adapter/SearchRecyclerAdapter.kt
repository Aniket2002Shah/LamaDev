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
import com.example.lamadev.R
import com.example.lamadev.databinding.ItemSearchBinding
import com.example.lamadev.pojo.Notification
import com.example.lamadev.pojo.Search

class SearchRecyclerAdapter():RecyclerView.Adapter<SearchRecyclerAdapter.RecyclerViewHolder>() {

    private  var  searchList:ArrayList<Search> = arrayListOf<Search>()

    class RecyclerViewHolder(private val binding: ItemSearchBinding, itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("PrivateResource")
        fun insert(item:Search){
                binding.searchProfilePic.setImageResource(com.google.android.material.R.drawable.ic_clock_black_24dp)
                binding.searchProfilePic.setColorFilter(R.color.gray)
                binding.username.text = item.content
                binding.work.text = item.createdAt.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<ItemSearchBinding>(inflater,
            R.layout.item_search,parent,false)

        return RecyclerViewHolder(binding,binding.root)
    }

    override fun getItemCount(): Int {
        return  searchList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
       val item = searchList[position]
        holder.insert(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onChange(item:List<Search>){
        searchList.clear()
        searchList.addAll(item)
        notifyDataSetChanged()

    }
}