package com.app.open.piccollab.core.network.module

import com.app.open.piccollab.core.auth.AuthManager
import com.app.open.piccollab.core.db.datastore.DataStorePref
import com.app.open.piccollab.core.network.module.apiservices.DriveApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


private const val DRIVE_BASE_URL = "https://www.googleapis.com/drive/v3/"

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): DriveApiService {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
        ).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(DRIVE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(DriveApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesAuthManager(dataStorePref: DataStorePref): AuthManager{
        return AuthManager(dataStorePref)
    }
}