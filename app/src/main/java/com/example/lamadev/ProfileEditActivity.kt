package com.example.lamadev

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.lamadev.databinding.ActivityProfileEditBinding
import com.example.lamadev.pojo.UpdateUser
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.LoginViewModel
import com.example.lamadev.view_model.UserEditViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class ProfileEditActivity: AppCompatActivity() {

    @Inject
    lateinit var userRep:UserRepository
    private lateinit var currentUser:User
    private lateinit var userEditViewModel: UserEditViewModel
    private  val iconTextList:ArrayList<String> =  ArrayList<String>()
    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var iconList:ArrayList<Int>
    var profileImage: Uri = Uri.EMPTY
    var coverImage: Uri = Uri.EMPTY


    private val profileContract = registerForActivityResult(ActivityResultContracts.GetContent()){
        binding.profilePicture.setImageURI(it)
        binding.progressProfile.visibility = View.GONE
        profileImage = it!!
    }

    private val coverContract = registerForActivityResult(ActivityResultContracts.GetContent()){
        binding.coverPhoto.setImageURI(it)
        binding.progressCover.visibility = View.GONE
        coverImage = it!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)

        binding.toolbar.title=""

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        //icon Declaration
        iconList = ArrayList<Int>()
        iconList.add(R.drawable.baseline_info_24)
        iconList.add(R.drawable.baseline_work_24)
        iconList.add(R.drawable.baseline_person_24)
        iconList.add(R.drawable.baseline_location_on_24)
        iconList.add(R.drawable.baseline_heart)

        iconTextList.add(0,"")
        iconTextList.add(1,"")
        iconTextList.add(2,"")
        iconTextList.add(3,"")
        iconTextList.add(4,"")

        userEditViewModel = ViewModelProvider(this)[UserEditViewModel::class.java]
        userEditViewModel.currentUser.observe(this){user->

            if(user.isNotEmpty()){
            if(user[0].email.isNotBlank() && user[0].password.isNotBlank()) {
                currentUser = user[0]
                binding.creationDate.text = user[0].createdAt.toString()

                if(user[0].username!=null){
                    binding.username.text = Editable.Factory.getInstance().newEditable(user[0].username)
                }

                if (user[0].bio != null) {
                    binding.bio.text = Editable.Factory.getInstance().newEditable(user[0].bio)
                }
                if (user[0].profilePicture != null) {
                    Glide.with(binding.profilePicture.context).load(user[0].profilePicture)
                        .into(binding.profilePicture)
                }
                if (user[0].coverPicture != null) {
                    Glide.with(binding.coverPhoto.context).load(user[0].coverPicture)
                        .into(binding.coverPhoto)
                }


                if (user[0].about != null) {
                    iconTextList.add(0,user[0].about!!)
                }

                if (user[0].work != null) {
                    iconTextList.add(1,user[0].work!!)
                }

                if (user[0].gender != null) {
                    iconTextList.add(2,user[0].gender!!)
                }

                if (user[0].address != null) {
                    iconTextList.add(3,user[0].address!!)
                }

                if (user[0].relationship != null) {
                    iconTextList.add(4,user[0].relationship!!)
                }
            }
            }
        }


        for(i in 0 until iconList.size){
            addResource(i)
        }


        userEditViewModel.updateUserLiveData.observe(this) {
            when(it) {

                is Response.Processing->{
                    binding.progressBar.visibility = View.VISIBLE

                }
                is Response.Success-> {
                    it?.let {
                        binding.progressBar.visibility = View.GONE
                    }
                }
                is Response.Error->{
                    it?.let {
                        binding.progressBar.visibility = View.GONE
                       Toast.makeText(this,it.r_message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.profilePictureEdit.setOnClickListener {
            profileContract.launch("image/*")
            binding.progressProfile.visibility = View.VISIBLE
        }

        binding.coverPhotoEdit.setOnClickListener {
            coverContract.launch("image/*")
            binding.progressCover.visibility = View.VISIBLE
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_edit_menu,menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId){
            R.id.correct->{
                val username = binding.username.text.toString()
                val bio  = binding.bio.text.toString()
                val about = binding.info.editText.text.toString()
                val work = binding.work.editText.text.toString()
                val gender = binding.gender.editText.text.toString()
                val address = binding.location.editText.text.toString()
                val relationship = binding.relationShip.editText.text.toString()

//                if(username!=currentUser.username || bio !=currentUser.bio || about != currentUser.about || work !=currentUser.work || gender != currentUser.gender || address!= currentUser.address || relationship!= currentUser.relationship ) {
                    userEditViewModel.updateUser(
                        currentUser.id, UpdateUser(currentUser.id, username, bio, about, work, gender, address, relationship))


                if(profileImage!= Uri.EMPTY){
                    val filesDir = applicationContext.filesDir
                    val file = File(filesDir,"profile.jpg")

                    val inputStream = contentResolver.openInputStream(profileImage)
                    val outputStream = FileOutputStream(file)
                    inputStream!!.copyTo(outputStream)


                    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val part = MultipartBody.Part.createFormData("image",file.name,requestBody)
                    userEditViewModel.updateProfilePhoto(part,currentUser.id)
                    inputStream.close()
                }
                if(coverImage!= Uri.EMPTY){
                    val filesDir = applicationContext.filesDir
                    val file = File(filesDir,"cover.jpg")

                    val inputStream = contentResolver.openInputStream(coverImage)
                    val outputStream = FileOutputStream(file)
                    inputStream!!.copyTo(outputStream)


                    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val part = MultipartBody.Part.createFormData("image",file.name,requestBody)
                    userEditViewModel.updateCoverPhoto(part,currentUser.id)
                    inputStream.close()
                }

                Toast.makeText(this,"Successfully created profile",Toast.LENGTH_SHORT).show()
                 startActivity(Intent(this, MainActivity::class.java))

                return true
            }
            else -> return false
        }
    }


    private fun addResource(int: Int){
        when(int) {
            0 -> {
                binding.info.apply {
                    icon.setImageResource(iconList[int])
                    editText.text = Editable.Factory.getInstance().newEditable(iconTextList[int])
                }
            }
            1 -> {
                binding.work.apply {
                    icon.setImageResource(iconList[int])
                    editText.text = Editable.Factory.getInstance().newEditable(iconTextList[int])
                }
            }
            2 -> {
                binding.gender.apply {
                    icon.setImageResource(iconList[int])
                    editText.text = Editable.Factory.getInstance().newEditable(iconTextList[int])
                }
            }
            3 -> {
                binding.location.apply {
                    icon.setImageResource(iconList[int])
                    editText.text = Editable.Factory.getInstance().newEditable(iconTextList[int])
                }
            }
            4 -> {
                binding.relationShip.apply {
                    icon.setImageResource(iconList[int])
                    editText.text = Editable.Factory.getInstance().newEditable(iconTextList[int])
                }
            }

        }
    }

//    private fun upload(image:Uri,dir:String): MultipartBody.Part{
//        val filesDir = applicationContext.filesDir
//        val file = File(filesDir,dir)
//
//        val inputStream = contentResolver.openInputStream(image)
//        val outputStream = FileOutputStream(file)
//        inputStream!!.copyTo(outputStream)
//
//
//        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
//        val part = MultipartBody.Part.createFormData("image",file.name,requestBody)
//        return part
//    }

}