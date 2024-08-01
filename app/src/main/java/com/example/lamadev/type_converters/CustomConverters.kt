package com.example.lamadev.type_converters

import androidx.room.TypeConverter
import com.example.lamadev.pojo.Comment
import com.example.lamadev.pojo.UserRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashSet

class CustomConverters() {

     private val gson: Gson = Gson()

    @TypeConverter
    fun setToInt(likesList:Set<UserRequest>):Int{
        return likesList.size
    }

    @TypeConverter
    fun intToSet(likes:Int):Set<UserRequest>{
         val likesList : Set<UserRequest>  = HashSet<UserRequest>()
        likesList.plus(UserRequest(likes,"like","lamaDB"))
        return likesList
    }

    @TypeConverter
    fun commentSetToInt(commentList:Set<Comment>):Int{
        return commentList.size
    }

    @TypeConverter
    fun intToCommentSet(comment:Int):Set<Comment>{
        val commentList : Set<Comment>  = HashSet<Comment>()
        commentList.plus(UserRequest(comment,"like","lamaDB"))
        return commentList
    }

    @TypeConverter
    fun userToString(userRequest: UserRequest):String{
        return gson.toJson(userRequest)
    }

    @TypeConverter
    fun stringToUser(userRequest: String):UserRequest{
       return gson.fromJson(userRequest,UserRequest::class.java)
    }

    @TypeConverter
    fun listToString( img : List<String>):String{
        return gson.toJson(img)
    }

    @TypeConverter
    fun stringToList( img : String):List<String>{
        val type = object : TypeToken<List<String>>(){}.type
        return gson.fromJson(img, type)
    }


    @TypeConverter
    fun dateToLong( date: Date):Long{
        return date.time
    }

    @TypeConverter
    fun longToDate( date:Long):Date{
        return Date(date)
    }

    @TypeConverter
    fun mapToString(chatOnline_At:Map<Int,Date>):String{
        return gson.toJson(chatOnline_At)
    }

    @TypeConverter
    fun stringToMap(chatOnline_At:String):Map<Int,Date>{
        val type = object : TypeToken<Map<Int,Date>>(){}.type
        return gson.fromJson(chatOnline_At,type)
    }

    @TypeConverter
    fun hasSetToString(chatUsers:HashSet<UserRequest>):String{
        return gson.toJson(chatUsers)
    }

    @TypeConverter
    fun stringToHashSet(chatUsers:String):HashSet<UserRequest>{
        val type = object : TypeToken<HashSet<UserRequest>>(){}.type
        return gson.fromJson(chatUsers,type)
    }
}