package com.app.open.piccollab.core.network.module

import com.app.open.piccollab.core.models.user.UserDriveDetail
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("about")
    suspend fun getUserDetails(@QueryMap queryMap: Map<String, String>): UserDriveDetail
}