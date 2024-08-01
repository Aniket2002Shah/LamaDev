package com.example.lamadev.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.lamadev.MainActivity
import com.example.lamadev.R
import com.example.lamadev.adapter.PostLoadImgRecyclerAdapter
import com.example.lamadev.databinding.FragmentAddPostBinding
import com.example.lamadev.databinding.PostLoadImgBinding
import com.example.lamadev.pojo.*
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.AddPostViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AddPostFragment(private val currentUser: User) : Fragment(),PostLoadImgRecyclerAdapter.OnItemClick {
      private  lateinit var  addImgBinding: PostLoadImgBinding
      private lateinit var binding: FragmentAddPostBinding
      private lateinit var addPostViewModel: AddPostViewModel
      private  lateinit var  iconTextList:ArrayList<Int>
      private lateinit var iconList:ArrayList<Int>
      private var postImgList:ArrayList<Uri> = ArrayList()
      private lateinit var imageUri: Uri
      private lateinit var adapter: PostLoadImgRecyclerAdapter
      var postId = 0


      private val contract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
          if(result.resultCode==Activity.RESULT_OK && result.data!=null){
              val data = result.data
              if( data?.clipData!=null){
                  val mClipData = data.clipData!!
                  for(i in 0 until  mClipData.itemCount){
                      val imgUri = mClipData.getItemAt(i).uri
                      postImgList.add(imgUri)
                  }

              }else{
                  val imgUri = data?.data!!
                  postImgList.add(imgUri)
              }

              adapter.onChange(postImgList,"gallery")
          }

      }



    private val camera = registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
        if (result) {
           postImgList.add(imageUri)
            adapter.onChange(postImgList,"camera")
            postImgList.clear()
        }
    }

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //iconText Declaration
        iconTextList = ArrayList<Int>()
        iconTextList.add(R.string.camera)
        iconTextList.add(R.string.gallery)
        iconTextList.add(R.string.videos)
        iconTextList.add(R.string.tag)
        iconTextList.add(R.string.live)
        iconTextList.add(R.string.location)
        iconTextList.add(R.string.audio)
        iconTextList.add(R.string.emoji)
        iconTextList.add(R.string.gif)
        iconTextList.add(R.string.template)
        iconTextList.add(R.string.text)
        iconTextList.add(R.string.theme)

        //icon Declaration
        iconList = ArrayList<Int>()
        iconList.add(R.drawable.icon_camera)
        iconList.add(R.drawable.icon_image)
        iconList.add(R.drawable.icon_video)
        iconList.add(R.drawable.icon_tag)
        iconList.add(R.drawable.icon_live)
        iconList.add(R.drawable.icon_map)
        iconList.add(R.drawable.icon_music)
        iconList.add(R.drawable.icon_emoji)
        iconList.add(R.drawable.icon_gif)
        iconList.add(R.drawable.icon_template)
        iconList.add(R.drawable.icon_text)
        iconList.add(R.drawable.icon_theme)
        // Inflate the layout for this fragment
        binding=  DataBindingUtil.inflate<FragmentAddPostBinding>(inflater,
            R.layout.fragment_add_post,container,false)

        Glide.with(binding.profilePic.context).load(currentUser.profilePicture).circleCrop()
            .into(binding.profilePic)

        for(i in 0..iconList.size-1){
            addResource(i)
        }

        addPostViewModel = ViewModelProvider(requireActivity())[AddPostViewModel::class.java]

        binding.imageListRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL,false)
        adapter = PostLoadImgRecyclerAdapter(this)
        binding.imageListRecyclerView.setHasFixedSize(true)
        binding.imageListRecyclerView.adapter= adapter

       // binding.toolbarActivity.title = "LamaDev"
       ////--- requireActivity().onCreateOptionsMenu(profileBinding.toolbarActivity.menu)
        //onCreateOptionsMenu(profileBinding.toolbarActivity.menu,MenuInflater(activity))

        addPostViewModel.stringLiveDataResp.observe(viewLifecycleOwner) {
            when(it) {

                is Response.Processing->{
                    binding.progressBarPost.visibility = View.VISIBLE

                }
                is Response.Success-> {
                    it?.let {
                            binding.progressBarPost.visibility = View.VISIBLE
                            postId = it.r_data!!.toInt()
                    }
                }
                is Response.Error->{
                    it?.let {
                        binding.progressBarPost.visibility = View.GONE
                        val snackBar= it.r_message?.let { message ->
                            Snackbar.make(
                                binding.layout, message, Snackbar.LENGTH_SHORT)
                        }
                        snackBar?.show()
                    }
                }
            }
        }


        addPostViewModel.postMutableLiveData.observe(viewLifecycleOwner) {
            when(it) {

                is Response.Processing->{
                    binding.progressBarPost.visibility = View.VISIBLE
                }
                is Response.Success-> {
                    it?.let {
                        binding.progressBarPost.visibility = View.VISIBLE
                    }
                }
                is Response.Error->{
                    it?.let {
                        binding.progressBarPost.visibility = View.GONE
                        val snackBar= it.r_message?.let { message ->
                            Snackbar.make(
                                binding.layout, message, Snackbar.LENGTH_SHORT)
                        }
                        snackBar?.show()
                    }
                }
            }
        }


        binding.Gallery.icon.setOnClickListener {
            getGalleryImage()
        }

        imageUri = getCameraImgUri()!!
        binding.Camera.icon.setOnClickListener {
            camera.launch(imageUri)
        }


        binding.postBtn.setOnClickListener {

            val title = binding.titleEditText.text.toString()!!
            val content = binding.postEditText.text.toString()!!
            val type = "NEWS"
            val view = "PUBLIC"

            if(title.isNotBlank()) {
                val post = Post2(title,content,  type, view)
                binding.layout.isEnabled = false
//            binding.progressBarPost.visibility= View.VISIBLE
                addPostViewModel.createPost(post,currentUser.id)

                if(postImgList.size>0) {
                    val image = uriToMultiPart(postImgList)
                    addPostViewModel.uploadPostImg(image, postId)
                }

                Toast.makeText(activity,"Successfully created post",Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity,MainActivity::class.java))

            }else{
                Snackbar.make(binding.layout, "Post is not valid", Snackbar.ANIMATION_MODE_FADE).show()
            }
        }

        return binding.root

    }

    private fun addResource(int: Int){
        when(int){
            0->{
                binding.Camera.apply {
                    icon.setImageResource(iconList[int])
                    iconName.text = getString(iconTextList[int])
                }
            }

            1->{
                binding.Gallery.apply {
                    icon.setImageResource(iconList[int])
                    iconName.text = getString(iconTextList[int])
                }
            }

            2->{
                binding.Videos.apply {
                    icon.setImageResource(iconList[int])
                    iconName.text = getString(iconTextList[int])
                }
            }

            3->{
                binding.Tag.apply {
                    icon.setImageResource(iconList[int])
                    iconName.text = getString(iconTextList[int])
                }
            }

            4->{
                binding.GoLive.apply {
                    icon.setImageResource(iconList[int])
                    iconName.text = getString(iconTextList[int])
                }
            }

            5->{
                binding.Location.apply {
                    icon.setImageResource(iconList[int])
                    iconName.text = getString(iconTextList[int])
                }
            }

             6->{
            binding.Audio.apply {
                icon.setImageResource(iconList[int])
                iconName.text = getString(iconTextList[int])
              }
            }

            7->{
                binding.Emoji.apply {
                    icon.setImageResource(iconList[int])
                    iconName.text = getString(iconTextList[int])
                }
            }

            8->{
                binding.Gif.apply {
                    icon.setImageResource(iconList[int])
                    iconName.text = getString(iconTextList[int])
                }
            }

            9->{
                binding.Template.apply {
                    icon.setImageResource(iconList[int])
                    iconName.text = getString(iconTextList[int])
                }
            }

            10->{
                binding.Text.apply {
                    icon.setImageResource(iconList[int])
                    iconName.text = getString(iconTextList[int])
                }
            }

            11->{
                binding.Theme.apply {
                    icon.setImageResource(iconList[int])
                    iconName.text = getString(iconTextList[int])
                }
            }
        }
    }

    private fun getGalleryImage(){
         val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        contract.launch(Intent.createChooser(intent,"Select pictures to be posted"))
    }

    private fun getCameraImgUri(): Uri? {
        val filesDir = activity?.applicationContext?.filesDir
        val file = File(filesDir,"camera_photo.jpg")
        return activity?.applicationContext?.let { FileProvider.getUriForFile(it,"com.example.lamadev.fileProvider",file) }
    }

    override fun onClick(img: Uri) {
        postImgList.remove(img)
        adapter.onChange(postImgList,"else")
    }

    private fun uriToMultiPart(postImageList:List<Uri>):List<MultipartBody.Part>{
        val multipartfile :List<MultipartBody.Part> = ArrayList()
        for(i in 0 until postImgList.size){
            val uri = postImgList[i]
            try {
                val filesDir = activity?.applicationContext?.filesDir
                val file = File(filesDir, UUID.randomUUID().toString()+".jpg")

                val inputStream = activity?.contentResolver?.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                inputStream!!.copyTo(outputStream)

                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("image",file.name,requestBody)
                multipartfile.plus(part)
                inputStream.close()
            }catch (illE:IllegalArgumentException){
                Log.d("Shingeki",illE.message.toString())
            }
        }
        Log.d("KimetsuNoYaiba",multipartfile.size.toString())
        return multipartfile
    }


}