package com.example.lamadev.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lamadev.ChatActivity
import com.example.lamadev.R
import com.example.lamadev.adapter.ChatRecyclerAdapter
import com.example.lamadev.databinding.FragmentChatBinding
import com.example.lamadev.pojo.Chat
import com.example.lamadev.pojo.Message
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.ChatRepository
import com.example.lamadev.repository.MessageRepository
import com.example.lamadev.repository.PostRepository
import com.example.lamadev.repository.StoryRepository
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.ChatContainerViewModel
import com.example.lamadev.view_model.ChatViewModel
import com.example.lamadev.view_model.HomeViewModel
import com.example.lamadev.view_model_factory.ChatContainerViewModelFactory
import com.example.lamadev.view_model_factory.ChatViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment(private val currentUser: User,private  val data:List<Message>) : Fragment(),ChatRecyclerAdapter.OnItemClicked {

    @Inject
    lateinit var chatRepository: ChatRepository


    private lateinit var chatViewModel: ChatViewModel
    private lateinit var viewModelFactory: ChatViewModelFactory
    private lateinit var binding: FragmentChatBinding
    private lateinit var adapter:ChatRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=  DataBindingUtil.inflate<FragmentChatBinding>(inflater,
            R.layout.fragment_chat,container,false)

        viewModelFactory  = ChatViewModelFactory(currentUser.id,chatRepository)
        chatViewModel = ViewModelProvider(this,viewModelFactory)[ChatViewModel::class.java]

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)

         adapter =ChatRecyclerAdapter(this,data,currentUser)
        binding.chatRecyclerView.setHasFixedSize(true)
        binding.chatRecyclerView.adapter= adapter
        binding.chatRecyclerView.isNestedScrollingEnabled = false


        chatViewModel.chatListLiveData.observe(viewLifecycleOwner) {
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
                       Toast.makeText(activity,it.r_message,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return binding.root

    }

    override fun onLayoutClicked(chat: Chat) {
//        chatContainerViewModel.getLatestMessages()
        val intent = Intent(activity,ChatActivity::class.java)
        intent.putExtra("currentUserId",currentUser.id)
        intent.putExtra("chat",chat)
        startActivity(intent)
        activity?.overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
    }
}