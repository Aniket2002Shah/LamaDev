package com.example.lamadev.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lamadev.R
import com.example.lamadev.databinding.PostLoadImgBinding

class PostLoadImgRecyclerAdapter(private val item: PostLoadImgRecyclerAdapter.OnItemClick): RecyclerView.Adapter<PostLoadImgRecyclerAdapter.RecyclerViewHolder>() {

    private  var  postList:ArrayList<Uri> = arrayListOf<Uri>()
    private  lateinit var imageType:String


    class RecyclerViewHolder(private val binding: PostLoadImgBinding, itemView: View,context: Context): RecyclerView.ViewHolder(itemView) {
        val context = context

        fun insert(item:Uri){
//            Image by Glide
            binding.image.setImageURI(item)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<PostLoadImgBinding>(inflater,
            R.layout.post_load_img,parent,false)

        //

//            val imageView = ImageView(parent.context)
//            imageView.setImageResource(R.drawable.ic_launcher_background)
//            val param = LinearLayout.LayoutParams(500, 300)
//            param.setMargins(20, 3, 20, 3)
//            imageView.layoutParams = param
//            binding.linearLayout.addView(imageView)


        val viewHolder =  RecyclerViewHolder(binding,binding.root,parent.context)
        binding.cancel.setOnClickListener {
            item.onClick(postList[viewHolder.adapterPosition])
        }
        return viewHolder

    }

    override fun getItemCount(): Int {
        return  postList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
       val item = postList[position]
        if(imageType=="camera") {
            val post = holder.itemView.findViewById<ImageView>(R.id.image)
            post.setImageURI(null)
        }
        holder.insert(item)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun onChange(item:List<Uri>,type:String){
        imageType = type
        postList.clear()
        postList.addAll(item)
        notifyDataSetChanged()

    }

    interface OnItemClick{
        fun onClick(post:Uri)
    }
}