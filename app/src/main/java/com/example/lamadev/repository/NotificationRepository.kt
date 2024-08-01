package com.example.lamadev.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lamadev.dependency_injection.RoomDatabase
import com.example.lamadev.pojo.Notification
import com.example.lamadev.pojo.UpdateUser
import com.example.lamadev.pojo.entities.NotificationEntity
import com.example.lamadev.response_handler.Response
import com.example.lamadev.utils.NetworkConnection
import com.example.lamadev.web_services.NotificationServices
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val notificationServices: NotificationServices, private  val roomDatabase: RoomDatabase, private val application: Context) {

    private var notifyMutableLiveData= MutableLiveData<Response<List<Notification>>>()
    val notifyLiveData: LiveData<Response<List<Notification>>>
        get()= notifyMutableLiveData


    private var userMutableLiveData= MutableLiveData<Response<String>>()

    val userLiveData: LiveData<Response<String>>
        get()= userMutableLiveData


    private var stringMutableLiveData= MutableLiveData<Response<String>>()
    val stringLiveData: LiveData<Response<String>>
        get()= stringMutableLiveData


    //registerUser
    suspend fun getAllNotification(userId:Int){
        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                notifyMutableLiveData.postValue(Response.Processing())
                val result = notificationServices.getAllNotificationByUserId(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data  = result.body()!!
                    roomDatabase.getNotificationDao().deleteNotification()
                    data.forEach{notify->
                        roomDatabase.getNotificationDao().addNotification(NotificationEntity(notify.id,notify.type,notify.type_id,notify.content,notify.createdAt,notify.updatedAt,notify.isRead,true))
                    }
                    notifyMutableLiveData.postValue(Response.Success(data))
                }
                else{
                    notifyMutableLiveData.postValue(Response.Error("Failed to fetch notification.Server not Responding !!"))
                }
            }
            catch (e:Exception){
                notifyMutableLiveData.postValue(Response.Error(e.message))
            }
        }else {
            try {
                val notifyList = ArrayList<Notification>()
                val notify =  roomDatabase.getNotificationDao().getAllNotification()
                notify.forEach{
                    notifyList.add(
                        Notification(it.id,it.type,it.type_id,it.content,null,null,it.createdAt,it.updatedAt,it.isRead)
                    )
                }
                notifyMutableLiveData.postValue(Response.Success(notifyList))

            } catch (e: Exception) {
                notifyMutableLiveData.postValue(Response.Error(e.message))
            }
        }
    }



    suspend fun findLatestNotificationByUser(userId:Int) {
        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                notifyMutableLiveData.postValue(Response.Processing())
                val result = notificationServices.findLatestNotificationByUserId(userId)
                if (result.body() != null && (result.code() >= 200) && (result.code() <= 299)) {
                    val data = result.body()!!
                    data.forEach { notify ->
                        roomDatabase.getNotificationDao().addNotification(
                            NotificationEntity(
                                notify.id,
                                notify.type,
                                notify.type_id,
                                notify.content,
                                notify.createdAt,
                                notify.updatedAt,
                                notify.isRead,
                                false
                            )
                        )
                    }
                    notifyMutableLiveData.postValue(Response.Success(data))
                } else {
                    notifyMutableLiveData.postValue(Response.Error("Failed to fetch notification.Server not Responding !!"))
                }
            } catch (e: Exception) {
                notifyMutableLiveData.postValue(Response.Error(e.message))
            }
        } else {
            notifyMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }



    suspend fun setIsRead(notificationId:Int, userId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                stringMutableLiveData.postValue(Response.Processing())
                val result = notificationServices.setIsRead(notificationId,userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data  = result.body()!!
                    roomDatabase.getNotificationDao().setIsRead(NotificationEntity(data.id,data.type,data.type_id,data.content,data.createdAt,data.updatedAt,data.isRead,true))
                }
                else{
                    stringMutableLiveData.postValue(Response.Error("Failed to update notification isRead. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                stringMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            stringMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }



    //update NotifyAt
    suspend fun updateNotifyAt(userId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                userMutableLiveData.postValue(Response.Processing())
                val result = notificationServices.updateUserNotifyAt(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
//                    roomDatabase.getUserDao().updateUser(result.body()!!)
                    userMutableLiveData.postValue(Response.Success("User notifyAt field  updated successfully !!"))
                }
                else{
                    userMutableLiveData.postValue(Response.Error("Failed to update  user notifyAt field . Server not Responding !!"))
                }
            }
            catch (e:Exception){
                userMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            userMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }
}