package com.example.lamadev.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lamadev.dependency_injection.RoomDatabase
import com.example.lamadev.pojo.Status
import com.example.lamadev.pojo.StatusDtoReq
import com.example.lamadev.pojo.Story
import com.example.lamadev.pojo.StoryDtoReq
import com.example.lamadev.response_handler.Response
import com.example.lamadev.utils.NetworkConnection
import com.example.lamadev.web_services.StoryServices
import okhttp3.MultipartBody
import java.util.*
import javax.inject.Inject

class StoryRepository @Inject constructor(private val storyServices: StoryServices, private  val roomDatabase: RoomDatabase, private val application: Context) {

    private var storyMutableLiveData = MutableLiveData<Response<String>>()

    val storyLiveData: LiveData<Response<String>>
        get() = storyMutableLiveData

    private var statusMutableLiveData = MutableLiveData<Response<List<Status>>>()

    val statusLiveData: LiveData<Response<List<Status>>>
        get() = statusMutableLiveData


    suspend fun createStory(story: Story, userId: Int) {

        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                storyMutableLiveData.postValue(Response.Processing())
                val result = storyServices.createStory(story, userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data  = result.body()!!
                    roomDatabase.getStatusDao().addStatus(StatusDtoReq(data.status.id,data.status.user))
                    roomDatabase.getStoryDao().addStory(StoryDtoReq(data.id,data.content,data.image,data.status.id,data.likes,data.createdAt,data.updatedAt))
                    storyMutableLiveData.postValue(Response.Success("Story created successfully"))
                } else {
                    storyMutableLiveData.postValue(Response.Error("Failed to create story.Server not Responding !!"))
                }
            } catch (e: Exception) {
                storyMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            statusMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun getAllStatus(userId: Int) {

        roomDatabase.getStoryDao().getAllStory().forEach{story->
            val date = Date(story.createdAt.time + 24 * 60 * 60 * 1000)
            if (Date() > date){
                roomDatabase.getStoryDao().deleteStory(story.id)
            }
        }
        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                statusMutableLiveData.postValue(Response.Processing())
                val result = storyServices.getAllStatusOfUserAndFollowings(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val value = result.body()!!
                    roomDatabase.getStoryDao().deleteStory()
                    roomDatabase.getStatusDao().deleteStatus()
                    value.forEach { status ->
                        roomDatabase.getStatusDao().addStatus(StatusDtoReq(status.id, status.user))
                        status.stories?.let {
                            it.forEach { story ->
                                roomDatabase.getStoryDao().addStory(
                                    StoryDtoReq(
                                        story.id,
                                        story.content,
                                        story.image,
                                        status.id,
                                        story.likes,
                                        story.createdAt,
                                        story.updatedAt
                                    )
                                )
                            }
                        }
                    }
                    //val storyReq: StoryDtoReq = StoryDtoReq(value.id,value.content,value.image,value.status.id,value.likes,value.createdAt,value.updatedAt)
//                    quotesDao.addQuotes(result.body()!!.results)
                    statusMutableLiveData.postValue(Response.Success(value))
                } else {
                    statusMutableLiveData.postValue(Response.Error("Failed to get stories.Server not Responding !!"))
                }
            } catch (e: Exception) {
                statusMutableLiveData.postValue(Response.Error(e.message))
            }
        } else {
            try {
                val statusList: List<Status> = ArrayList()
                val status = roomDatabase.getStatusDao().getAllStatus()
                status.forEach { s ->
                    val stories = roomDatabase.getStoryDao().getStoryByStatusId(s.id)
                    if(stories.isNotEmpty()) {
                        statusList.plus(Status(s.id, s.user, stories))
                    }
                }
                statusMutableLiveData.postValue(Response.Success(statusList))

            } catch (e: Exception) {
                statusMutableLiveData.postValue(Response.Error(e.message))
            }
        }

    }


    suspend fun updateStory(story:Story, storyId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                storyMutableLiveData.postValue(Response.Processing())
                val result = storyServices.updateStory(story,storyId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data = result.body()!!
                    roomDatabase.getStoryDao().updateStory(StoryDtoReq(data.id,data.content,data.image,data.status.id,data.likes,data.createdAt,data.updatedAt))
                    storyMutableLiveData.postValue(Response.Success("Story updated successfully"))
                }
                else{
                    storyMutableLiveData.postValue(Response.Error("Failed to update  Story . Server not Responding !!"))
                }
            }
            catch (e:Exception){
                storyMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            storyMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun uploadStoryImage(image:MultipartBody.Part, storyId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                storyMutableLiveData.postValue(Response.Processing())
                val result = storyServices.uploadStoryImage(image,storyId)

                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data = result.body()!!
                    roomDatabase.getStoryDao().updateStory(StoryDtoReq(data.id,data.content,data.image,data.status.id,data.likes,data.createdAt,data.updatedAt))
                    storyMutableLiveData.postValue(Response.Success("Image uploaded successfully"))
                }
                else{
                    storyMutableLiveData.postValue(Response.Error("Failed to upload story image. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                storyMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            storyMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun likeOrDislikeStory(storyId: Int,userId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                storyMutableLiveData.postValue(Response.Processing())
                val result = storyServices.likeOrDislikeStory(storyId,userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data = result.body()!!
                    roomDatabase.getStoryDao().updateStory(StoryDtoReq(data.id,data.content,data.image,data.status.id,data.likes,data.createdAt,data.updatedAt))
                    storyMutableLiveData.postValue(Response.Success("liked story successfully"))
                }
                else{
                    storyMutableLiveData.postValue(Response.Error("Failed to upload like story. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                storyMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            storyMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }

    suspend fun deleteStory(storyId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                storyMutableLiveData.postValue(Response.Processing())
                val result = storyServices.deleteStory(storyId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data = result.body()!!
                    roomDatabase.getStoryDao().deleteStory(storyId)
                    storyMutableLiveData.postValue(Response.Success("deleted story successfully"))
                }
                else{
                    storyMutableLiveData.postValue(Response.Error("Failed to upload delete story. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                storyMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            storyMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }
}