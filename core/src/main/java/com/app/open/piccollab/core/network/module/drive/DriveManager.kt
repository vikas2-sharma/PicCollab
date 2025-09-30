package com.app.open.piccollab.core.network.module.drive

import android.content.Context
import android.util.Log
import com.app.open.piccollab.core.auth.AuthManager
import com.app.open.piccollab.core.db.datastore.DataStorePref
import com.app.open.piccollab.core.db.room.entities.EventFolder
import com.app.open.piccollab.core.db.room.repositories.DEFAULT_PROJECT_FOLDER_NAME
import com.app.open.piccollab.core.models.event.NewEventItem
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Date

private const val TAG = "DriveManager"


class DriveManager(
    private val context: Context,
    private val dataStorePref: DataStorePref,
    private val authManager: AuthManager
) {
    val tokenMutex = Mutex()

    @Volatile
    private var _token: String = ""
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    private val token: String
        get() = runBlocking {
            tokenMutex.withLock {
                _token
            }
        }

    init {
        coroutineScope.launch {
            dataStorePref.getAccessToken().collect { token ->
                tokenMutex.withLock {
                    Log.d(TAG, "token: $token")
                    _token = token
                }
            }
        }
    }

    suspend fun createFolder(eventItem: NewEventItem, projectFolderId: String? = null): String {
        Log.d(
            TAG,
            "createFolder() called with: eventItem = $eventItem, projectFolderId = $projectFolderId"
        )
        val file = File()
        file.mimeType = "application/vnd.google-apps.folder"
        file.name = eventItem.eventName
        file.description = eventItem.eventDescription
        file.parents = listOf(projectFolderId)
        try {
            val outFile = getDriveService()?.files()?.create(file)?.execute()
            Log.d(TAG, "createFolder() returned: id: ${outFile?.id}")
            return outFile?.id ?: ""
        } catch (e: Exception) {
            Log.w(TAG, "createFolder: ", e)
            return ""
        }
    }


    private suspend fun getDriveService(): Drive? {
        Log.d(TAG, "getDriveService() called")
        val driveService: Drive = try {
            /*checking accessToken expiry*/
            val expiryTime = dataStorePref.getExpiresIn()
            val currentTime = Date().time

            Log.d(TAG, "getDriveService: expiryTime: $expiryTime")
            Log.d(TAG, "getDriveService: currentTime: $currentTime")
            if (currentTime > expiryTime) {
                Log.d(TAG, "getDriveService: getting new token")
                authManager.getNewToken()
                Log.d(TAG, "getDriveService: new token received")
            }


            val accessToken = AccessToken(token, null)
            val googleCredential = GoogleCredentials.create(accessToken)

            val requestInitializer = HttpCredentialsAdapter(googleCredential)
            Drive.Builder(
                NetHttpTransport(), GsonFactory.getDefaultInstance(), requestInitializer
            ).setApplicationName(context.packageName).build()
        } catch (e: Exception) {
            Log.d(TAG, "getDriveService: ", e)
            return null
        }
        return driveService
    }

/*
    fun cancelDriveManagerCoroutine() {
        coroutineScope.cancel()
    }
*/

    suspend fun rootFolderId(): String? {
        Log.d(TAG, "rootFolderId() called")
        val fileList = try {
            getDriveService()?.files()?.list()?.setQ(
                "mimeType='application/vnd.google-apps.folder'"
            )?.setFields("files(id, name, createdTime, mimeType)")?.execute()?.files
        } catch (e: Exception) {
            Log.w(TAG, "rootFolderId: ", e)
            return null
        }
        if (fileList != null) {
            Log.d(TAG, "rootFolderId: files: $fileList")
            val rootFolder = fileList.find { file -> file.name == DEFAULT_PROJECT_FOLDER_NAME }
            val rootFolderId = rootFolder?.id
            Log.d(TAG, "rootFolderId() returned: $rootFolderId")
            return rootFolderId
        } else {
            return null
        }
    }

    suspend fun getEventFolderFromDrive(rootProjectId: String): List<EventFolder> {
        Log.d(TAG, "getEventFolderFromDrive: ")
        val driveService = getDriveService()
        if (driveService != null) {

            val fileList = try {
                driveService.files().list().setQ(
                    "'$rootProjectId' in parents and mimeType='application/vnd.google-apps.folder'"
                ).setFields("files(id, name, createdTime, mimeType, description)").execute().files
            } catch (e: Exception) {
                Log.w(TAG, "getEventFolderFromDrive: ", e)
                return emptyList()
            }
            val eventFolderList = mutableListOf<EventFolder>()
            fileList.forEach { file ->
                eventFolderList.add(EventFolder(file.id, file.name, file.description))
            }
            Log.d(TAG, "getEventFolderFromDrive: event folder list $eventFolderList")
            return eventFolderList
        } else {
            return emptyList()
        }
    }

}