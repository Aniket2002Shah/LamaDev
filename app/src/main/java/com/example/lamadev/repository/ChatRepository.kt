package com.example.lamadev.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lamadev.dependency_injection.RoomDatabase
import com.example.lamadev.pojo.*
import com.example.lamadev.response_handler.Response
import com.example.lamadev.utils.NetworkConnection
import com.example.lamadev.web_services.ChatRoomServices
import com.example.lamadev.web_services.StoryServices
import okhttp3.MultipartBody
import java.util.*
import javax.inject.Inject

class ChatRepository @Inject constructor(private val chatRoomServices: ChatRoomServices, private  val roomDatabase: RoomDatabase, private val application: Context) {

    private var chatMutableLiveData = MutableLiveData<Response<Chat>>()
    val chatLiveData: LiveData<Response<Chat>>
        get() = chatMutableLiveData

    private var stringMutableLiveData = MutableLiveData<Response<String>>()
    val stringLiveData: LiveData<Response<String>>
        get() = stringMutableLiveData


    private var chatListMutableLiveData = MutableLiveData<Response<List<Chat>>>()
    val chatListLiveData: LiveData<Response<List<Chat>>>
        get() = chatListMutableLiveData


    suspend fun createChatRoom(userRequest: UserRequest, userId: Int) {

        if (NetworkConnection.isInternetAvailable(application)) {
            try {
               chatMutableLiveData.postValue(Response.Processing())
                val result = chatRoomServices.createChatRoom(userRequest,userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data  = result.body()!!
                    roomDatabase.getChatDao().addChat(data)
                    chatMutableLiveData.postValue(Response.Success(data))
                } else {
                    chatMutableLiveData.postValue(Response.Error("Failed to create Chatroom.Server not Responding !!"))
                }
            } catch (e: Exception) {
                chatMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            chatMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun createGroup(groupChatRequest: GroupChatRequest, userId: Int) {

        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                chatMutableLiveData.postValue(Response.Processing())
                val result = chatRoomServices.createGroup(groupChatRequest,userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data  = result.body()!!
                    roomDatabase.getChatDao().addChat(data)
                    chatMutableLiveData.postValue(Response.Success(data))
                } else {
                    chatMutableLiveData.postValue(Response.Error("Failed to create Chatroom.Server not Responding !!"))
                }
            } catch (e: Exception) {
                chatMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            chatMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun getAllChatRoom(userId: Int) {

        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                chatListMutableLiveData.postValue(Response.Processing())
                val result = chatRoomServices.getAllChatOfUserById(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val value = result.body()!!
                    roomDatabase.getChatDao().addChat(value)
                    chatListMutableLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    chatListMutableLiveData.postValue(Response.Error("Failed to get stories.Server not Responding !!"))
                }
            } catch (e: Exception) {
                chatListMutableLiveData.postValue(Response.Error(e.message))
            }
        } else {
            try {
                val status = roomDatabase.getChatDao().getChats()
                chatListMutableLiveData.postValue(Response.Success(status))

            } catch (e: Exception) {
                chatListMutableLiveData.postValue(Response.Error(e.message))
            }
        }

    }

    suspend fun updateOnlineAtOfUser(userId: Int,chatRoomId:Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                chatMutableLiveData.postValue(Response.Processing())
                val result = chatRoomServices.updateOnlineAtOfUser(userId,chatRoomId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data = result.body()!!
                    roomDatabase.getChatDao().updateChat(data)
                    chatMutableLiveData.postValue(Response.Success(data))
                }
                else{
                    chatMutableLiveData.postValue(Response.Error("Failed to add user to Chat!!"))
                }
            }
            catch (e:Exception){
                chatMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            chatMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun addUserToGroup(userId: Int,chatRoomId:Int,userRequest: UserRequest){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                chatMutableLiveData.postValue(Response.Processing())
                val result = chatRoomServices.addUserToGroup(userId, chatRoomId, userRequest)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data = result.body()!!
                    roomDatabase.getChatDao().updateChat(data)
                    chatMutableLiveData.postValue(Response.Success(data))
                }
                else{
                    chatMutableLiveData.postValue(Response.Error("Failed to add user to Chat!!"))
                }
            }
            catch (e:Exception){
                chatMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            chatMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun removeUserFromGroup(chatRoomId: Int,userId: Int,userRequest: UserRequest){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                chatMutableLiveData.postValue(Response.Processing())
                val result = chatRoomServices.removeUserFromGroup(chatRoomId,userId,userRequest)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data = result.body()!!
                    roomDatabase.getChatDao().updateChat(data)
                    chatMutableLiveData.postValue(Response.Success(data))
                }
                else{
                    chatMutableLiveData.postValue(Response.Error("Failed to remove User from Chat !!"))
                }
            }
            catch (e:Exception){
                chatMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            chatMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun renameGroup(chatRoomId: Int,userId: Int,groupname:String){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                chatMutableLiveData.postValue(Response.Processing())
                val result = chatRoomServices.renameGroup(chatRoomId, userId, groupname)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data = result.body()!!
                   roomDatabase.getChatDao().updateChat(data)
                    chatMutableLiveData.postValue(Response.Success(data))
                }
                else{
                    chatMutableLiveData.postValue(Response.Error("Failed to rename group!!"))
                }
            }
            catch (e:Exception){
                chatMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            chatMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }

    suspend fun deleteGroup(chatRoomId: Int,userId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                stringMutableLiveData.postValue(Response.Processing())
                val result = chatRoomServices.deleteGroupByUser(chatRoomId, userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data = result.body()!!
                    roomDatabase.getChatDao().deleteGroupChat(chatRoomId)
                    stringMutableLiveData.postValue(Response.Success("Successfully deleted chat"))
                }
                else{
                    stringMutableLiveData.postValue(Response.Error("Failed to upload delete group !!"))
                }
            }
            catch (e:Exception){
                stringMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            stringMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }
}