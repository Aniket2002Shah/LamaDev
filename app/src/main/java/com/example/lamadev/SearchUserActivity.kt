package com.example.lamadev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lamadev.adapter.SearchListRecyclerAdapter
import com.example.lamadev.databinding.ActivitySearchUserBinding
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.SearchRepository
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.SearchViewModel
import com.example.lamadev.view_model_factory.SearchViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchUserActivity : AppCompatActivity(),SearchListRecyclerAdapter.OnItemClick {
    @Inject
    lateinit var searchRepository: SearchRepository

    private  var currentuserId:Int = 0
    private lateinit var keyword :String
    private  lateinit var searchViewModel: SearchViewModel
    private lateinit var viewModelFactory: SearchViewModelFactory
    private lateinit var binding: ActivitySearchUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= DataBindingUtil.setContentView(this, R.layout.activity_search_user)

            currentuserId = intent.extras!!.getInt("currentuser")
            keyword = intent.extras!!.getString("keyword")!!
            viewModelFactory  = SearchViewModelFactory(currentuserId,searchRepository)
            searchViewModel= ViewModelProvider(this,viewModelFactory)[SearchViewModel::class.java]

            searchViewModel.search(keyword)

            binding.searchRecyclerView.layoutManager = LinearLayoutManager(this)

            val adapter = SearchListRecyclerAdapter(this)
            binding.searchRecyclerView.setHasFixedSize(true)
            binding.searchRecyclerView.adapter= adapter
            binding.searchRecyclerView.isNestedScrollingEnabled = false
            searchViewModel.userSearchListLiveData.observe(this) {
                when(it) {

                    is Response.Processing->{
                        binding.progressBar.visibility = View.VISIBLE

                    }
                    is Response.Success-> {
                        it?.let {
                            binding.progressBar.visibility = View.GONE

                            val data = it.r_data!!
                            Log.d("Shingekinokyojin",data.toString())
                            adapter.onChange(data)
                        }
                    }
                    is Response.Error->{
                        it?.let {
                            binding.progressBar.visibility = View.GONE

                            Toast.makeText(this,it.r_message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    override fun onClick(user: User) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("currentUserId",currentuserId)
        intent.putExtra("profileUserId",user.id)
        startActivity(intent)
    }

}