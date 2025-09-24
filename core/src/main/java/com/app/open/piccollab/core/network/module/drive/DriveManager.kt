package com.app.open.piccollab.core.network.module.drive

import android.content.Context
import android.util.Log
import com.app.open.piccollab.core.db.datastore.DataStorePref
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Date

private const val TAG = "DriveManager"


class DriveManager(private val context: Context, dataStorePref: DataStorePref) {
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
                    _token = token
                }
            }
        }
    }

    fun createFolder(eventItem: NewEventItem): String {
        val file = File()
        file.mimeType = "application/vnd.google-apps.folder"
        file.name = eventItem.eventName
        file.description = eventItem.eventDescription

        val outFile = getDriveService().files().create(file).execute()
        Log.d(TAG, "createFolder() returned: id: ${outFile.id}")
        return outFile.id
    }

    private fun getDriveService(): Drive {
        Log.d(TAG, "getDriveService() called")
        val accessToken = AccessToken(token, Date(Date().time + 300_000))
        val googleCredential = GoogleCredentials.create(accessToken)

        val requestInitializer = HttpCredentialsAdapter(googleCredential)
        val driveService: Drive = Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            requestInitializer
        ).setApplicationName(context.packageName).build()
        return driveService
    }

    fun cancelDriveManagerCoroutine() {
        coroutineScope.cancel()
    }

}