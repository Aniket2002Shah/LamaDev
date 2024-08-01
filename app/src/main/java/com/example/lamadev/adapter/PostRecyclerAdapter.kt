package com.example.lamadev.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lamadev.R
import com.example.lamadev.databinding.ItemPostBinding
import com.example.lamadev.pojo.Post
import com.example.lamadev.pojo.Status

class PostRecyclerAdapter(private val item: PostRecyclerAdapter.OnItemClick): RecyclerView.Adapter<PostRecyclerAdapter.RecyclerViewHolder>() {

    private  var  postList:ArrayList<Post> = arrayListOf<Post>()


    class RecyclerViewHolder(private val binding: ItemPostBinding, itemView: View,context: Context): RecyclerView.ViewHolder(itemView) {
        val context = context

        fun insert(item:Post){
//            Image by Glide
            Glide.with(binding.postProfilePic.context).load(item.user.profilePicture).circleCrop()
                .into(binding.postProfilePic)
            binding.postUserName.text = item.user.username
            binding.time.text = item.createdAt.toString()
            binding.post.text = item.content
            if(item.likes.isNotEmpty()) {
                binding.likes.text = item.likes.size.toString()
            }else{
                binding.likes.text = "0"
            }

            if(item.likes.isNotEmpty()) {
                binding.comment.text = item.comments.size.toString()
            }else{
                binding.comment.text = "0"
            }

            if(item.likes.isNotEmpty()) {
                binding.share.text = item.share.size.toString()
            }else{
                binding.share.text = "0"
            }

            if(item.img.isNotEmpty()) {
                item.img.forEach {
                    val imageView = ImageView(context)
                    val param = LinearLayout.LayoutParams(450, 200)
                    param.setMargins(50, 3, 40, 3)
                    imageView.layoutParams = param
                    Glide.with(imageView.context).load(it).circleCrop()
                        .into(imageView)
                    binding.linearLayout.addView(imageView)

                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<ItemPostBinding>(inflater,
            R.layout.item_post,parent,false)

        //

//            val imageView = ImageView(parent.context)
//            imageView.setImageResource(R.drawable.ic_launcher_background)
//            val param = LinearLayout.LayoutParams(500, 300)
//            param.setMargins(20, 3, 20, 3)
//            imageView.layoutParams = param
//            binding.linearLayout.addView(imageView)


        val viewHolder =  RecyclerViewHolder(binding,binding.root,parent.context)
        binding.postProfilePic.setOnClickListener {
            item.onClick(postList[viewHolder.adapterPosition])
        }
        return viewHolder

    }

    override fun getItemCount(): Int {
        return  postList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
       val item = postList[position]
        holder.insert(item)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun onChange(item:List<Post>){
        postList.clear()
        postList.addAll(item)
        notifyDataSetChanged()

    }

    interface OnItemClick{
        fun onClick(post:Post)
    }
}