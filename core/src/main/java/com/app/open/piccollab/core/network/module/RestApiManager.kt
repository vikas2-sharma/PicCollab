package com.app.open.piccollab.core.network.module

import android.util.Log
import com.app.open.piccollab.core.models.user.UserDriveDetail
import com.app.open.piccollab.core.network.module.apiservices.DriveApiService
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject


private const val TAG = "RestApiManager"

class RestApiManager @Inject constructor(
    private val driveApiService: DriveApiService
) {

    companion object {
        var accessToken: String? = null
    }

    suspend fun getUserDetails(queryMap: HashMap<String, String>): UserDriveDetail {

        val headerMap = HashMap<String, String>().apply {
            put("Authorization", "Bearer $accessToken")
            put("Accept", "application/json")
        }

        Log.d(TAG, "getUserDetails: $headerMap")
        return driveApiService.getUserDetails(queryMap, headerMap)
    }
}