package com.example.lamadev.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.lamadev.*
import com.example.lamadev.adapter.PostRecyclerAdapter
import com.example.lamadev.adapter.StoryRecyclerAdapter
import com.example.lamadev.databinding.FragmentHomeBinding
import com.example.lamadev.dependency_injection.RoomDatabase
import com.example.lamadev.pojo.*
import com.example.lamadev.repository.PostRepository
import com.example.lamadev.repository.StoryRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.HomeViewModel
import com.example.lamadev.view_model_factory.HomeViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

@AndroidEntryPoint
class HomeFragment(private val currentUser: User) : Fragment(),StoryRecyclerAdapter.OnItemClick,PostRecyclerAdapter.OnItemClick {
    @Inject
    lateinit var storyRepository: StoryRepository
    @Inject
    lateinit var postRepository: PostRepository

    @Inject
    lateinit var userRepository:UserRepository

    private lateinit var viewModelFactory: HomeViewModelFactory
     private lateinit var homeBinding: FragmentHomeBinding
     private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home, container, false
        )

        viewModelFactory  = HomeViewModelFactory(currentUser.id,storyRepository, postRepository,userRepository)
        homeViewModel = ViewModelProvider(this,viewModelFactory)[HomeViewModel::class.java]

        homeBinding.labelUsername.text = currentUser.username
        Glide.with(homeBinding.profilePic.context).load(currentUser.profilePicture).circleCrop()
            .into(homeBinding.profilePic)

        //Story
//        arrayList = ArrayList<Story>()
//        arrayList.add(Story("y",1,1,"John"))
//        arrayList.add(Story("y",2,2,"Mohan"))
//        arrayList.add(Story("y",4,4,"Nathan"))
//        arrayList.add(Story("y",5,5,"Suparana Krishnaswami Reddy"))
//        arrayList.add(Story("y",6,6,"Michael Keton"))
//        arrayList.add(Story("y",7,7,"Thomas Cruise"))
//        arrayList.add(Story("y",3,3,"Rock"))

        homeBinding.storyRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        val adapter = StoryRecyclerAdapter(this)
        homeBinding.storyRecyclerView.setHasFixedSize(true)
        homeBinding.storyRecyclerView.adapter= adapter
        homeViewModel.statusLiveData.observe(viewLifecycleOwner) {
            when(it) {

                is Response.Processing->{
                    homeBinding.progressBar.visibility = View.VISIBLE

                }
                is Response.Success-> {
                    it?.let {
                        homeBinding.progressBar.visibility = View.GONE

                        val data = it.r_data!!
                        Log.d("shingekinokyojin", data.toString())
                        if(data.isNotEmpty()) {
                            adapter.onChange(data)
                        }else{
                            val storyList = ArrayList<StoryDtoReq>()
                            storyList.add(StoryDtoReq(0,"",currentUser.coverPicture!!,currentUser.id,HashSet(),currentUser.createdAt,currentUser.updatedAt ))

                            val arrayList = ArrayList<Status>()
                            arrayList.add(Status(0, UserRequest(currentUser.id,currentUser.username,currentUser.profilePicture),storyList))
                            adapter.onChange(arrayList)
                        }
                    }
                }
                is Response.Error->{
                    it?.let {
                        homeBinding.progressBarPost.visibility = View.GONE

                        val arrayList = ArrayList<Status>()
                        val storyList = ArrayList<StoryDtoReq>()
                        storyList.add(StoryDtoReq(0,"",currentUser.coverPicture!!,currentUser.id,HashSet(),currentUser.createdAt,currentUser.updatedAt ))
                        arrayList.add(Status(0, UserRequest(currentUser.id,currentUser.username,currentUser.profilePicture),storyList))
                        adapter.onChange(arrayList)
                    }
                }
            }
        }


        homeBinding.postRecyclerView.layoutManager = LinearLayoutManager(context)
        val postAdapter = PostRecyclerAdapter(this)
        homeBinding.postRecyclerView.setHasFixedSize(true)
        homeBinding.postRecyclerView.adapter= postAdapter
        homeViewModel.postLiveData.observe(viewLifecycleOwner) {
            when(it) {

                is Response.Processing->{
                    homeBinding.progressBarPost.visibility = View.VISIBLE
                    homeBinding.errorPage.errorLayout.visibility = View.GONE

                }
                is Response.Success-> {
                    it?.let {
                        homeBinding.progressBarPost.visibility = View.GONE
                        homeBinding.errorPage.errorLayout.visibility = View.GONE

                        val data = it.r_data!!
                        if(data.isNotEmpty()) {
                            postAdapter.onChange(data)
                        }else{
                            homeBinding.errorPage.errorLayout.visibility = View.VISIBLE
                            Snackbar.make(homeBinding.homeLayout, "Your feed is empty",Snackbar.ANIMATION_MODE_SLIDE).show()

                            homeBinding.errorPage.refreshBtn.setOnClickListener {
                                homeViewModel.loadPost()
                            }
                        }

                    }
                }
                is Response.Error->{
                    it?.let {
                        homeBinding.progressBarPost.visibility = View.GONE

                        homeBinding.errorPage.errorLayout.visibility = View.VISIBLE

                        val snackBar= it.r_message?.let { message ->
                            Snackbar.make(
                                homeBinding.homeLayout, message,Snackbar.LENGTH_SHORT)
                        }
                        snackBar?.show()
                        Log.d("Tokyoghoul",it.r_message!!)
                        homeBinding.errorPage.refreshBtn.setOnClickListener {
                            homeViewModel.loadPost()
                        }
                    }
                }
            }
        }

        homeBinding.profilePic.setOnClickListener {
//            setCurrentFragment(ProfileFragment(currentUser.id, currentUser.id))
            val intent = Intent(activity, ProfileActivity::class.java)
            intent.putExtra("currentUserId",currentUser.id)
            intent.putExtra("profileUserId",currentUser.id)
            startActivity(intent)
        }

        homeBinding.logout.setOnClickListener {
           homeViewModel.logout()
            startActivity(Intent(activity,LoginActivity::class.java))
            activity?.finish()
            activity?.overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom, androidx.appcompat.R.anim.abc_slide_out_bottom)
        }

        return homeBinding.root
    }
//
//    private fun setCurrentFragment(fragment: Fragment) {
//        val fragmentTrans = requireActivity().supportFragmentManager.beginTransaction()
//        fragmentTrans.replace(R.id.frame_layout,fragment)
//        fragmentTrans.commit()
//        }

    override fun onClick(story: Status) {
        startActivity(Intent(activity,StoryActivity::class.java))
    }

    override fun onClick(post: Post) {
        val intent = Intent(activity, ProfileActivity::class.java)
        intent.putExtra("currentUserId",currentUser.id)
        intent.putExtra("profileUserId",post.user.id)
        startActivity(intent)
    }


}