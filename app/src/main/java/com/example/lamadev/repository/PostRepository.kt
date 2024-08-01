package com.example.lamadev.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lamadev.dependency_injection.RoomDatabase
import com.example.lamadev.pojo.*
import com.example.lamadev.response_handler.Response
import com.example.lamadev.utils.NetworkConnection
import com.example.lamadev.web_services.PostServices
import okhttp3.MultipartBody
import javax.inject.Inject

class PostRepository@Inject constructor(private val postServices: PostServices, private  val roomDatabase: RoomDatabase, private val application: Context) {

    private var postMutableLiveData = MutableLiveData<Response<Post>>()
    val postLiveData: LiveData<Response<Post>>
        get() = postMutableLiveData

    //
    private var postListMutableLiveData = MutableLiveData<Response<List<Post>>>()
    val postListLiveData: LiveData<Response<List<Post>>>
        get() = postListMutableLiveData

//
    private var stringMutableLiveData = MutableLiveData<Response<String>>()
    val stringLiveData: LiveData<Response<String>>
        get() = stringMutableLiveData

    //
    private var commentMutableLiveData = MutableLiveData<Response<Comment>>()
    val commentLiveData: LiveData<Response<Comment>>
        get() = commentMutableLiveData


    suspend fun createPost(post: Post2, userId: Int) {

        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                stringMutableLiveData.postValue(Response.Processing())
                val result = postServices.createPost(post,userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data  = result.body()!!
                    roomDatabase.getPostDao().addPost(result.body()!!)
                    stringMutableLiveData.postValue(Response.Success(data.id.toString()))
                } else {
                    stringMutableLiveData.postValue(Response.Error("Failed to create post.Server not Responding !!"))
                }
            } catch (e: Exception) {
                stringMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            stringMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun getTimeline(userId: Int) {
        if (NetworkConnection.isInternetAvailable(application)) {
            try {
                postListMutableLiveData.postValue(Response.Processing())
                val result = postServices.getUserTimeline(userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val value = result.body()!!
                    roomDatabase.getPostDao().deletePost()
                    roomDatabase.getPostDao().addPost(result.body()!!)
                    postListMutableLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    postListMutableLiveData.postValue(Response.Error("Failed to get timeline.Server not Responding !!"))
                }
            } catch (e: Exception) {
                postListMutableLiveData.postValue(Response.Error(e.message))
            }
        } else {
            try {
                val postList =  roomDatabase.getPostDao().getAllPost()
                postListMutableLiveData.postValue(Response.Success(postList))

            } catch (e: Exception) {
                postListMutableLiveData.postValue(Response.Error(e.message))
            }
        }

    }


    suspend fun updatePost(post: Post, postId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                postMutableLiveData.postValue(Response.Processing())
                val result = postServices.updatePost(post,postId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    roomDatabase.getPostDao().updatePost(result.body()!!)
                    postMutableLiveData.postValue(Response.Success(result.body()!!))
                }
                else{
                    postMutableLiveData.postValue(Response.Error("Failed to update  Post.Server not Responding !!"))
                }
            }
            catch (e:Exception){
                postMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            postMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun uploadPostImage(image: List<MultipartBody.Part>, postId: Int){
        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                postMutableLiveData.postValue(Response.Processing())
                val result = postServices.uploadPostImage(image,postId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    roomDatabase.getPostDao().updatePost(result.body()!!)
                    postMutableLiveData.postValue(Response.Success(result.body()!!))
                }
                else{
                    postMutableLiveData.postValue(Response.Error("Failed to upload post image. Server not Responding !!"))
                }
            }
            catch (e:Exception){
                postMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            postMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun likeOrDislikePost(postId: Int,userId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                stringMutableLiveData.postValue(Response.Processing())
                val result = postServices.likeOrDislikePost(postId,userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    roomDatabase.getPostDao().updatePost(result.body()!!)
                }
                else{
                    stringMutableLiveData.postValue(Response.Error("Failed to upload like user.Server not Responding !!"))
                }
            }
            catch (e:Exception){
                stringMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            stringMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun sharePost(postId: Int,userId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                stringMutableLiveData.postValue(Response.Processing())
                val result = postServices.sharePost(postId,userId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    roomDatabase.getPostDao().updatePost(result.body()!!)
                }
                else{
                    stringMutableLiveData.postValue(Response.Error("Failed to upload share user.Server not Responding !!"))
                }
            }
            catch (e:Exception){
                stringMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            stringMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }

    suspend fun createComment(comment: Comment,postId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                commentMutableLiveData.postValue(Response.Processing())
                val result = postServices.createComment(comment,postId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    commentMutableLiveData.postValue(Response.Success(result.body()!!))
                }
                else{
                    commentMutableLiveData.postValue(Response.Error("Failed to comment on Post.Server not Responding !!"))
                }
            }
            catch (e:Exception){
                commentMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            commentMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun deleteComment(commentId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                stringMutableLiveData.postValue(Response.Processing())
                val result = postServices.deleteComment(commentId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    stringMutableLiveData.postValue(Response.Success("Comment deleted successfully"))
                }
                else{
                    stringMutableLiveData.postValue(Response.Error("Failed to comment on Post.Server not Responding !!"))
                }
            }
            catch (e:Exception){
                stringMutableLiveData.postValue(Response.Error(e.message))
            }
        }else{
            stringMutableLiveData.postValue(Response.Error(("Check your Internet connection")))
        }
    }


    suspend fun deletePost(postId: Int){

        if(NetworkConnection.isInternetAvailable(application)) {
            try {
                stringMutableLiveData.postValue(Response.Processing())
                val result = postServices.deletePost(postId)
                if (result.body() != null && (result.code()>=200) && (result.code()<=299)) {
                    val data = result.body()!!
                    roomDatabase.getPostDao().deletePost(postId)
                    stringMutableLiveData.postValue(Response.Success("deleted post successfully"))
                }
                else{
                    stringMutableLiveData.postValue(Response.Error("Failed to upload delete post. Server not Responding !!"))
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