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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lamadev.R
import com.example.lamadev.SearchUserActivity
import com.example.lamadev.adapter.SearchRecyclerAdapter
import com.example.lamadev.databinding.FragmentSearchBinding
import com.example.lamadev.pojo.*
import com.example.lamadev.repository.SearchRepository
import com.example.lamadev.repository.StoryRepository
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.HomeViewModel
import com.example.lamadev.view_model.SearchViewModel
import com.example.lamadev.view_model_factory.HomeViewModelFactory
import com.example.lamadev.view_model_factory.SearchViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment(private val currentUser: User) : Fragment() {

    @Inject
    lateinit var searchRepository: SearchRepository

    private  lateinit var searchViewModel: SearchViewModel
    private lateinit var viewModelFactory: SearchViewModelFactory
    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=  DataBindingUtil.inflate<FragmentSearchBinding>(inflater,
            R.layout.fragment_search,container,false)

        viewModelFactory  = SearchViewModelFactory(currentUser.id,searchRepository)
        searchViewModel= ViewModelProvider(this,viewModelFactory)[SearchViewModel::class.java]


        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = SearchRecyclerAdapter()
        binding.searchRecyclerView.setHasFixedSize(true)
        binding.searchRecyclerView.adapter= adapter
        binding.searchRecyclerView.isNestedScrollingEnabled = false
        searchViewModel.searchListLiveData.observe(viewLifecycleOwner) {
            when(it) {

                is Response.Processing->{
                    binding.progressBar.visibility = View.VISIBLE

                }
                is Response.Success-> {
                    it?.let {
                        binding.progressBar.visibility = View.GONE

                        val data = it.r_data!!
                        adapter.onChange(data)
                    }
                }
                is Response.Error->{
                    it?.let {
                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(activity,it.r_message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.searchBtn.setOnClickListener {
            val keyword = binding.searchText.text.toString()
            if(keyword.isNotBlank()) {
                val intent = Intent(activity,SearchUserActivity::class.java)
                intent.putExtra("currentuser",currentUser.id)
                intent.putExtra("keyword",keyword)
                startActivity(intent)
                activity?.overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            }
            else{
                Toast.makeText(activity,"Search is not valid !!",Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}