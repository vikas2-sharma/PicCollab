package com.app.open.piccollab.core.network.upload

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.app.open.piccollab.core.utils.FileUtil
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials


private const val TAG = "FileUploader"

class FileUploader(private val context: Context) {


    fun fileDriveUpload(token: String, imageUri: String) {

        try {

            Log.d(TAG, "fileDriveUpload() called with: filePath = $imageUri")
            val driveService: Drive? = getDriveService(token)


            val folderId: String? = getFolderId(driveService)

            val imageFile = java.io.File(imageUri)

            val fileMetaData = File()
            fileMetaData.name = imageFile.name
            val mimeType = context.contentResolver.getType(imageFile.toUri())
            fileMetaData.mimeType = mimeType

            val parentFolder = listOf(folderId)

            fileMetaData.parents = parentFolder

            val mediaContent = FileContent(
                mimeType, imageFile

            )


            val file: File? = driveService?.files()?.create(fileMetaData, mediaContent)
                ?.setFields("id")
                ?.execute()

            val fileId = file?.id
            Log.d(TAG, "fileDriveUpload: fileId: $fileId")
        } catch (e: Exception) {
            Log.w(TAG, "fileDriveUpload: ", e)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    e.localizedMessage ?: "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getFolderId(driveService: Drive?): String? {
        val folderList = driveService?.files()?.list()
        Log.d(TAG, "fileDriveUpload: folderSize: ${folderList?.size}")

        var folderId: String? = null
        if (folderList != null) {
            val list = folderList.setQ(

                "mimeType = 'application/vnd.google-apps.folder'"
            )
                .setFields("files(id, name)")
                .execute()

            Log.d(TAG, "fileDriveUpload: list size: ${list.size}")
            list.files.forEach { file ->
                Log.d(TAG, "fileDriveUpload: Filenames: ${file.name}")
                folderId = file.id

            }
        }
        return folderId
    }

    private fun getDriveService(token: String): Drive? {
        val accessToken = AccessToken(token, null)
        val googleCredential = GoogleCredentials.create(accessToken)

        val requestInitializer = HttpCredentialsAdapter(googleCredential)
        val driveService: Drive? = Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            requestInitializer
        ).setApplicationName(context.packageName).build()
        return driveService
    }

    fun uploadFileUsingUri(token: String, uri: Uri) {

        Log.d(TAG, "uploadFileUsingUri() called with: token = $token, uri = $uri")
        val driveService: Drive? = getDriveService(token)


        val folderId: String? = getFolderId(driveService)

        val imageFile: java.io.File = (FileUtil.getFileFromUri(context, uri))

        val fileMetaData = File()
        val mimeType = context.contentResolver.getType(imageFile.toUri())
        fileMetaData.name = FileUtil.createImageFileName(context, uri)
        fileMetaData.mimeType = mimeType

        val parentFolder = listOf(folderId)

        fileMetaData.parents = parentFolder

        val mediaContent = FileContent(
            mimeType, imageFile

        )
        val file: File? = driveService?.files()?.create(fileMetaData, mediaContent)
            ?.setFields("id")
            ?.execute()

        val fileId = file?.id
        Log.d(TAG, "fileDriveUpload: fileId: $fileId")
    }
}
