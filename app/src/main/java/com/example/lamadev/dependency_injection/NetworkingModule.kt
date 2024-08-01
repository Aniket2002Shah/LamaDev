package com.example.lamadev.dependency_injection

import com.example.lamadev.utils.Constants
import com.example.lamadev.web_services.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkingModule {

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }
    @Singleton
    @Provides
    fun getUserServices(retrofit: Retrofit): UserServices {
        return retrofit.create(UserServices::class.java)
    }

    @Singleton
    @Provides
    fun getStoryServices(retrofit: Retrofit): StoryServices {
        return retrofit.create(StoryServices::class.java)
    }

    @Singleton
    @Provides
    fun getSearchServices(retrofit: Retrofit): SearchServices {
        return retrofit.create(SearchServices::class.java)
    }

    @Singleton
    @Provides
    fun getPostServices(retrofit: Retrofit): PostServices {
        return retrofit.create(PostServices::class.java)
    }

    @Singleton
    @Provides
    fun getNotificationServices(retrofit: Retrofit): NotificationServices {
        return retrofit.create(NotificationServices::class.java)
    }

    @Singleton
    @Provides
    fun getMessageServices(retrofit: Retrofit): MessageServices {
        return retrofit.create(MessageServices::class.java)
    }

    @Singleton
    @Provides
    fun getChatRoomServices(retrofit: Retrofit): ChatRoomServices {
        return retrofit.create(ChatRoomServices::class.java)
    }

    @Singleton
    @Provides
    fun getProfileServices(retrofit: Retrofit): ProfileServices {
        return retrofit.create(ProfileServices::class.java)
    }
}