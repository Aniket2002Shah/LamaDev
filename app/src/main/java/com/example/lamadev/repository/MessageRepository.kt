package com.example.lamadev.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lamadev.dependency_injection.RoomDatabase
import com.example.lamadev.pojo.*
import com.example.lamadev.pojo.entities.MessageEntity
import com.example.lamadev.response_handler.Response
import com.example.lamadev.utils.NetworkConnection
import com.example.lamadev.web_services.MessageServices
import javax.inject.Inject
import kotlin.collections.ArrayList

class MessageRepository @Inject constructor(private val messageServices: MessageServices, private  val roomDatabase: RoomDatabase, private val application: Context) {

    private var stringMutableLiveData = MutableLiveData<Response<String>>()
    val stringLiveData: LiveData<Response<String>>
        get() = stringMutableLiveData

    private var newMsgMutableLiveData = MutableLiveData<Response<Message>>()
    val newMsgLiveData: LiveData<Response<Message>>
        get() = newMsgMutableLiveData

    private var msgMutableLiveData = MutableLiveData<Response<List<Message>>>()
    val msgLiveData: LiveData<Response<List<Message>>>
        get() = msgMutableLiveData


    suspend fun sendMessage(message: Message) {
        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                newMsgMutableLiveData.postValue(Response.Processing())
                val result = messageServices.sendMessage(message)
                if (result.body() != null && (result.code() >= 200) && (result.code() <= 299)) {
                    val data = result.body()!!
                    roomDatabase.getMessageDao().addMessages(MessageEntity(data.id!!,data.content,data.createdAt!!,data.user,data.chat.id,true))
                    newMsgMutableLiveData.postValue(Response.Success(data))
                } else {
                    newMsgMutableLiveData.postValue(Response.Error("Failed to send message!!"))
                }
            } catch (e: Exception) {
                newMsgMutableLiveData.postValue(Response.Error(e.message))
            }
        } else {
            newMsgMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun getAllMessage(userId: Int,chatId:Int) {
        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                msgMutableLiveData.postValue(Response.Processing())
                val result = messageServices.getChatMessages(userId,chatId)
                if (result.body() != null && (result.code() >= 200) && (result.code() <= 299)) {
                    val data = result.body()!!
                    roomDatabase.getMessageDao().deleteMessagesByChatId(chatId)
                    data.forEach{message->
                        roomDatabase.getMessageDao().addMessages(MessageEntity(message.id!!,message.content,message.createdAt!!,message.user,message.chat.id,true))
                    }
                    msgMutableLiveData.postValue(Response.Success(data))
                } else {
                    msgMutableLiveData.postValue(Response.Error("Failed to fetch chat messages!!"))
                }
            } catch (e: Exception) {
                msgMutableLiveData.postValue(Response.Error(e.message))
            }
        } else {
            try {
                val messageList = ArrayList<Message>()
                val messageEntityList =  roomDatabase.getMessageDao().getMessagesByChatId(chatId)
                messageEntityList.forEach{message->
                    val chat = roomDatabase.getChatDao().getChatById(message.chatId)
                    val chatDetail = ChatDetail(chat.id,chat.chat_name,chat.chat_image)
                    messageList.add(Message(message.id,message.content,message.createdAt,message.user,chatDetail))
                }
                msgMutableLiveData.postValue(Response.Success(messageList))

            } catch (e: Exception) {
                msgMutableLiveData.postValue(Response.Error(e.message))
            }
        }
    }


    suspend fun getLatestMessagesOfUser(useId: Int) {
        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                msgMutableLiveData.postValue(Response.Processing())
                val result = messageServices.getLatestMessagesOfUser(useId)
                if (result.body() != null && (result.code() >= 200) && (result.code() <= 299)) {
                    val data = result.body()!!
                    data.forEach{message->
                        roomDatabase.getMessageDao().addMessages(MessageEntity(message.id!!,message.content,message.createdAt!!,message.user,message.chat.id,false))
                    }
                    msgMutableLiveData.postValue(Response.Success(data))
                } else {
                    msgMutableLiveData.postValue(Response.Error("Failed to fetch chat messages!!"))
                }
            } catch (e: Exception) {
                msgMutableLiveData.postValue(Response.Error(e.message))
            }
        }else {
            try {
                val messageList = ArrayList<Message>()
                val messageEntityList =  roomDatabase.getMessageDao().getUnseenMessages()
                messageEntityList.forEach{message->
                    val chat = roomDatabase.getChatDao().getChatById(message.chatId)
                    val chatDetail = ChatDetail(chat.id,chat.chat_name,chat.chat_image)
                    messageList.add(Message(message.id,message.content,message.createdAt,message.user,chatDetail))
                }
                msgMutableLiveData.postValue(Response.Success(messageList))

            } catch (e: Exception) {
                msgMutableLiveData.postValue(Response.Error(e.message))
            }
        }
    }

    suspend fun deleteMessage(request: UserRequest,messageId:Int) {
        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                stringMutableLiveData.postValue(Response.Processing())
                val result = messageServices.deleteMessages(request,messageId)
                if (result.body() != null && (result.code() >= 200) && (result.code() <= 299)) {
                    roomDatabase.getMessageDao().deleteMessageById(messageId)
                } else {
                    stringMutableLiveData.postValue(Response.Error("Failed to delete message!!"))
                }
            } catch (e: Exception) {
                stringMutableLiveData.postValue(Response.Error(e.message))
            }
        } else {
            stringMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }
}