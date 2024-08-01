package com.example.lamadev.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lamadev.R
import com.example.lamadev.databinding.StoryStatusRvBinding
import com.example.lamadev.pojo.Status

class StoryRecyclerAdapter(private val item:OnItemClick):
    RecyclerView.Adapter<StoryRecyclerAdapter.RecyclerViewHolder>() {

    private  var  statusList:ArrayList<Status> = arrayListOf<Status>()

    class RecyclerViewHolder(private val binding: StoryStatusRvBinding, itemView: View): RecyclerView.ViewHolder(itemView) {
        fun insert(item:Status){
            Log.d("jujutsu",item.toString())
//            Image by Glide
                Glide.with(binding.createStatusImg.context).load(item.stories?.get(0)?.image)
                    .into(binding.createStatusImg)

               Glide.with(binding.profilePic.context).load(item.user.profilePicture).circleCrop()
                    .into(binding.profilePic)

                binding.statusUsername.text = item.user.username
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<StoryStatusRvBinding>(inflater,
            R.layout.story_status_rv,parent,false)
        val viewHolder = RecyclerViewHolder(binding,binding.root)
        binding.cardViewCreateStatus.setOnClickListener {
            item.onClick(statusList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
       return  statusList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
       val item = statusList[position]
        holder.insert(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onChange(item:List<Status>){
        statusList.clear()
        statusList.addAll(item)
        notifyDataSetChanged()

    }

    interface OnItemClick{
        fun onClick(story:Status)
    }
}