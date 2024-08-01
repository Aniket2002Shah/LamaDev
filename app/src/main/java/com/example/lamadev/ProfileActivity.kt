package com.example.lamadev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.lamadev.adapter.FollowingRecyclerAdapter
import com.example.lamadev.databinding.ActivityProfileBinding
import com.example.lamadev.pojo.Chat
import com.example.lamadev.pojo.UserRequest
import com.example.lamadev.repository.ChatRepository
import com.example.lamadev.repository.ProfileRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.ProfileViewModel
import com.example.lamadev.view_model_factory.ProfileViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: ProfileRepository
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var chatRepository: ChatRepository

    private var i = 0
    private var profileUserId: Int = 0
    private var currentUserId: Int = 0
    private lateinit var chat:Chat
    private var isFollowing = false
    private var currentuserFollowing = ArrayList<UserRequest>()
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profiViewModelFactory: ProfileViewModelFactory
    private lateinit var profileBinding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        currentUserId = intent.extras!!.getInt("currentUserId")
        profileUserId = intent.extras!!.getInt("profileUserId")

        profileBinding.toolbarActivity.title = "LamaDev"
        setSupportActionBar(profileBinding.toolbarActivity)
        //requireActivity().onCreateOptionsMenu(profileBinding.toolbarActivity.menu)

        if (profileUserId == currentUserId) {
            profileBinding.follow.visibility = View.GONE
            //profileBinding.follow.isEnabled = false

            profileBinding.message.visibility = View.GONE
           // profileBinding.message.isEnabled = false
        }

        profiViewModelFactory =
            ProfileViewModelFactory(profileUserId, currentUserId, repository, userRepository,chatRepository)
        profileViewModel =
            ViewModelProvider(this, profiViewModelFactory)[ProfileViewModel::class.java]
        //onCreateOptionsMenu(profileBinding.toolbarActivity.menu,MenuInflater(activity))

        profileViewModel.currentUserFollowingLiveDataResp.observe(this) {
            when (it) {
                is Response.Processing -> {}
                is Response.Success -> {
                    it?.let {
                        val data = it.r_data!!
                        currentuserFollowing.addAll(data.followings)
                    }
                }
                is Response.Error -> {
                    Log.d("Monster", "Error in loading currentUserFollowing list")
                }
            }
        }



        profileViewModel.profileUser.observe(this) {

            when (it) {

                is Response.Processing -> {
                    profileBinding.progressBar.visibility = View.VISIBLE
                    profileBinding.errorPage.errorLayout.visibility = View.INVISIBLE

                }
                is Response.Success -> {
                    it?.let {
                        profileBinding.progressBar.visibility = View.GONE

                        val data = it.r_data!!

                        profileBinding.coverPic.visibility = View.VISIBLE
                        profileBinding.profilePic.visibility = View.VISIBLE
                        profileBinding.profileUsername.visibility = View.VISIBLE
                        profileBinding.userInfo.visibility = View.VISIBLE
                        profileBinding.userGender.visibility = View.VISIBLE
                        profileBinding.location.visibility = View.VISIBLE
                        profileBinding.relationShip.visibility = View.VISIBLE
                        profileBinding.profileLinearLayout1.visibility = View.VISIBLE
                        profileBinding.profileLinearLayout2.visibility = View.VISIBLE
                        profileBinding.bio.visibility = View.VISIBLE
                        profileBinding.followingText.visibility = View.VISIBLE
                        profileBinding.floatingActionBtn.visibility = View.VISIBLE


                        if (currentuserFollowing.contains(UserRequest(data.id, data.username, data.profilePicture))) {
                            isFollowing = true
                            profileBinding.follow.setImageResource(R.drawable.baseline_person_remove_24)
                            profileBinding.floatingActionBtn.visibility = View.GONE
                        }


                        Glide.with(profileBinding.coverPic.context).load(data.coverPicture)
                            .into(profileBinding.coverPic)
                        Glide.with(profileBinding.profilePic.context).load(data.profilePicture)
                            .circleCrop()
                            .into(profileBinding.profilePic)
                        profileBinding.profileUsername.text = data.username
                        profileBinding.userInfo.text = data.about
                        profileBinding.userGender.text = data.gender
                        profileBinding.location.text = data.address
                        profileBinding.relationShip.text = data.relationship
                        profileBinding.bio.text = data.bio

                        profileBinding.workValue.text = data.work
                    }
                }
                is Response.Error -> {
                    it?.let {
                        profileBinding.progressBar.visibility = View.GONE

                        profileBinding.errorPage.errorLayout.visibility = View.VISIBLE
                        profileBinding.errorPage.refreshBtn.setOnClickListener {
                            profileViewModel.loadProfile()
                        }


                    }
                }
            }
        }


        profileBinding.recyclerViewFollowing.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )

        val adapter = FollowingRecyclerAdapter()
        profileBinding.recyclerViewFollowing.setHasFixedSize(true)
        profileBinding.recyclerViewFollowing.adapter = adapter
        profileBinding.recyclerViewFollowing.isNestedScrollingEnabled = false
        profileViewModel.followingLiveDataResp.observe(this) {
            when (it) {

                is Response.Processing -> {
                    profileBinding.progressBarFollowing.visibility = View.VISIBLE

                }
                is Response.Success -> {
                    it?.let {
                        profileBinding.progressBarFollowing.visibility = View.GONE
                        val data = it.r_data!!
                        adapter.onChange(data.followings)

                    }
                }
                is Response.Error -> {
                    it?.let {
                        profileBinding.progressBarFollowing.visibility = View.VISIBLE
                        Toast.makeText(this, it.r_message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        profileBinding.floatingActionBtn.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
//            onDestroy()
        }

        profileBinding.follow.setOnClickListener {
            isFollowing = !isFollowing
            if (isFollowing) {
                profileBinding.follow.setImageResource(R.drawable.baseline_person_remove_24)
                profileViewModel.followUser(currentUserId)
            } else {
                profileBinding.follow.setImageResource(R.drawable.baseline_person_add_alt_1_24)
                profileViewModel.unFollowUser(currentUserId)
            }
        }


        profileViewModel.chatLiveData.observe(this) {
            when (it) {
                is Response.Processing -> {
                    profileBinding.progressBar.visibility = View.VISIBLE
                }
                is Response.Success -> {
                    it?.let {
                        profileBinding.progressBar.visibility = View.GONE
                        val data = it.r_data!!
                        chat = data
                        if(chat.id!=0) {
                            val intent = Intent(this, ChatActivity::class.java)
                            intent.putExtra("currentUserId", currentUserId)
                            intent.putExtra("chat", chat)
                            startActivity(intent)
                            this.overridePendingTransition(
                                androidx.appcompat.R.anim.abc_fade_in,
                                androidx.appcompat.R.anim.abc_fade_out
                            )
                        }
                    }
                }
                is Response.Error -> {
                    profileBinding.progressBar.visibility = View.GONE
                    Toast.makeText(this,"Unable to make chat some error",Toast.LENGTH_SHORT).show()
                    Log.d("Monster", "Error in loading currentUserFollowing list")
                }
            }
        }



        profileBinding.message.setOnClickListener {
            profileViewModel.createChatRoom()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu,menu)
        return true

    }
}



