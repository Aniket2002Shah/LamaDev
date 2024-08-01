package com.example.lamadev

import android.app.ActivityOptions
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LiveData
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.UserRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LamaDevApplication:Application() {
}