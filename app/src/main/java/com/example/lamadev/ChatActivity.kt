package com.example.lamadev

import android.content.DialogInterface
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.lamadev.adapter.MessageListRecyclerAdapter
import com.example.lamadev.databinding.ActivityChatBinding
import com.example.lamadev.pojo.*
import com.example.lamadev.repository.ChatRepository
import com.example.lamadev.repository.MessageRepository
import com.example.lamadev.repository.ProfileRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.MessageViewModel
import com.example.lamadev.view_model_factory.MessageViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity(),MessageListRecyclerAdapter.OnItemClicked {

    @Inject
    lateinit var messageRepository: MessageRepository
    @Inject
    lateinit var chatRepository: ChatRepository

    private var currentUserId:Int =0
    private lateinit var chat: Chat
    private lateinit var messageViewModel: MessageViewModel
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

         chat = if (VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
             intent?.getParcelableExtra("chat", Chat::class.java)!!
         }else{
             intent?.getParcelableExtra("chat")!!
         }
        currentUserId = intent?.extras!!.getInt("currentUserId")

         messageViewModel = ViewModelProvider(this, MessageViewModelFactory(currentUserId,chat.id, messageRepository,chatRepository))[MessageViewModel::class.java]

        binding.toolbarActivity.title = ""
        binding.toolbarActivity.overflowIcon = AppCompatResources.getDrawable(this,R.drawable.baseline_menu_vert_black_24)

        if(chat.isGroup){
            if(chat.chat_image!=null) {
                Glide.with(binding.chatImg.context).load(chat.chat_image).circleCrop()
                    .into(binding.chatImg)
           }
            else{
                binding.chatImg.setImageResource(R.drawable.outline_person_outline_24)
            }

            binding.chatName.text= chat.chat_name
        }else{
            chat.users.forEach {user->
                if(user.id!=currentUserId){
                    if(user.profilePicture!=null) {
                        Glide.with(binding.chatImg.context).load(user.profilePicture).circleCrop()
                            .into(binding.chatImg)
                    }
                    else{
                        binding.chatImg.setImageResource(R.drawable.outline_person_outline_24)
                    }
                    binding.chatName.text= user.username
                }
            }
        }

        binding.chatRecycler.layoutManager = LinearLayoutManager(this)

//        messageList.add(Message(1,2,"Hii","10:00","July 11"))
//        messageList.add(Message(2,1,"Hii","10:01","July 11"))
//        messageList.add(Message(3,2,"kkr","10:01","July 11"))



        val adapter = MessageListRecyclerAdapter(this,this,chat.id,currentUserId)
        binding.chatRecycler.setHasFixedSize(true)
        binding.chatRecycler.adapter= adapter
        binding.chatRecycler.isNestedScrollingEnabled = false

        messageViewModel.msgLiveData.observe(this) {
            when(it) {
                is Response.Processing->{}
                is Response.Success-> {
                    it?.let {
                        val data = it.r_data!!
                        adapter.onChange(data)
                        if(adapter.itemCount>0) {
                            binding.chatRecycler.smoothScrollToPosition(adapter.itemCount - 1)
                        }
                    } }
                is Response.Error->{ it?.let { Toast.makeText(this,it.r_message, Toast.LENGTH_LONG).show() } } } }

        //binding.chatRecycler.scrollToPosition(data.size)

        messageViewModel.stringLiveData.observe(this){
            when(it) {
                is Response.Processing->{}
                is Response.Success-> {
                    it?.let {
                        val data = it.r_data!!
                        Toast.makeText(this,data,Toast.LENGTH_SHORT)
                    } }
                is Response.Error->{ it?.let { Toast.makeText(this,it.r_message, Toast.LENGTH_LONG).show() } } } }



        messageViewModel.newMsgChatLiveData.observe(this){
            when(it) {
                is Response.Processing->{}
                is Response.Success-> {
                    it?.let {
                        val data = it.r_data!!
                        adapter.onChange(data)
                        binding.chatRecycler.smoothScrollToPosition(adapter.itemCount-1)
                    } }
                is Response.Error->{ it?.let { Toast.makeText(this,it.r_message, Toast.LENGTH_LONG).show() } } } }


        binding.chatText.addTextChangedListener {
            if (it.isNullOrBlank()) {
                binding.sendMic.setImageResource(R.drawable.baseline_mic_24)
            }
            else{
                binding.sendMic.setImageResource(R.drawable.baseline_send_24)
                binding.sendMic.setOnClickListener {
                    messageViewModel.sendMessage(Message(
                        content = binding.chatText.text.toString(),
                        user = UserRequest(
                        currentUserId,null,null),
                        chat = ChatDetail(id = chat.id,null,null),
                        createdAt = null, id = null))

                    binding.chatText.text = Editable.Factory.getInstance().newEditable("")
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu,menu)
        return true

    }

    override fun onLayoutClicked(message: Message) {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage("Do you want to delete this message ?")
        alertBuilder.setTitle("Delete")
        alertBuilder.setCancelable(true)
        alertBuilder.setPositiveButton("Yes") { dialog, which ->
            message.id?.let { messageViewModel.deleteMessage(it) }
        }

        val alertDialog = alertBuilder.create()
        alertDialog.show()
    }

    override fun onPause() {
        super.onPause()
        messageViewModel.updateOnlineAt()
        messageViewModel.chatLiveData.observe(this){
            when(it) {
                is Response.Processing->{}
                is Response.Success-> {
                    it?.let {} }
                is Response.Error->{ it?.let { Toast.makeText(this,it.r_message, Toast.LENGTH_LONG).show() } } } }
    }


}