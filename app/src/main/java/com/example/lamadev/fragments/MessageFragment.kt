package com.example.lamadev.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.lamadev.R
import com.example.lamadev.adapter.ViewPagerAdapter
import com.example.lamadev.databinding.FragmentMessageBinding
import com.example.lamadev.pojo.Message
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.ChatRepository
import com.example.lamadev.repository.MessageRepository
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.ChatContainerViewModel
import com.example.lamadev.view_model.ChatViewModel
import com.example.lamadev.view_model_factory.ChatContainerViewModelFactory
import com.example.lamadev.view_model_factory.ChatViewModelFactory

import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessageFragment(private val currentUser: User) : Fragment() {

    @Inject
    lateinit var messageRepository: MessageRepository
    private  lateinit var data:List<Message>
    private lateinit var chatViewModel: ChatContainerViewModel
    private lateinit var viewModelFactory: ChatContainerViewModelFactory
   private  lateinit var binding: FragmentMessageBinding
    private lateinit var adapter: ViewPagerAdapter
    private  val tabllayoutList = arrayOf("Chat","Group","Calls")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=  DataBindingUtil.inflate<FragmentMessageBinding>(inflater,
            R.layout.fragment_message,container,false)

       // chatId = activity?.intent?.getIntExtra("chatId",0)!!
        viewModelFactory  = ChatContainerViewModelFactory(currentUser.id,messageRepository)
        chatViewModel = ViewModelProvider(this,viewModelFactory)[ChatContainerViewModel::class.java]


        //requireActivity().onCreateOptionsMenu(profileBinding.toolbarActivity.menu)
        binding.toolbarActivity.overflowIcon = activity?.let { AppCompatResources.getDrawable(it,R.drawable.baseline_menu_vert_24) }
        onCreateOptionsMenu(binding.toolbarActivity.menu,MenuInflater(activity))

        chatViewModel.msgLiveData.observe(viewLifecycleOwner){
            when(it) {

                is Response.Processing->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Response.Success-> {
                    binding.progressBar.visibility = View.GONE
                    it?.let { msg ->
                        data = msg.r_data!!
                        adapter = ViewPagerAdapter(this)
                        adapter.addFragment(ChatFragment(currentUser,data))
                        adapter.addFragment(CommunityFragment(currentUser,data))
                        adapter.addFragment(CallFragment())
                        binding.viewPage.let {it->
                            it.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                            it.adapter = adapter
                        }

                        TabLayoutMediator(binding.tablayout,binding.viewPage){tab,position->
                            tab.text = tabllayoutList[position]
                        }.attach()

                    }
                }
                is Response.Error->{
                    binding.progressBar.visibility = View.VISIBLE
                    it?.let {
                        Toast.makeText(activity,it.r_message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return binding.root

    }

//    override fun onCreateContextMenu(
//        menu: ContextMenu,
//        v: View,
//        menuInfo: ContextMenu.ContextMenuInfo?
//    ) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//        activity?.menuInflater?.inflate(R.menu.message_menu_item,menu)
//    }
//
//    override fun registerForContextMenu(view: View) {
//        super.registerForContextMenu(view)
//        binding.toolbarActivity.menu
//    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.message_menu_item,menu)

    }
}