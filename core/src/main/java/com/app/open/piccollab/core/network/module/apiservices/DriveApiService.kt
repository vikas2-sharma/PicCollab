package com.app.open.piccollab.core.network.module.apiservices

import com.app.open.piccollab.core.models.user.UserDriveDetail
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.QueryMap

interface DriveApiService {

    @GET("about")
    suspend fun getUserDetails(@QueryMap query: Map<String, String>, @HeaderMap headers: Map<String, String>): UserDriveDetail


}