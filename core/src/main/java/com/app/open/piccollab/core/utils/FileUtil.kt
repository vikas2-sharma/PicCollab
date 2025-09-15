package com.app.open.piccollab.core.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.io.path.createTempFile
import kotlin.io.path.outputStream

private const val TAG = "FileUtil"
object FileUtil {
    fun moveAssetToStorage(context: Context, assetName: String): File? {
        Log.d(TAG, "moveAssetToStorage() called with: context = $context, assetName = $assetName")
        return try {
            val inputStream = context.assets.open(assetName)

            val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ?: return null

            if (!picturesDir.exists()) {
                picturesDir.mkdirs()
            }

            val outFile = File(picturesDir, assetName)

            FileOutputStream(outFile).use { output ->
                inputStream.copyTo(output)
            }

            inputStream.close()

            outFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createImageFileName(context: Context, uri: Uri): String {
        Log.d(TAG, "createImageFileName() called with: context = $context, uri = $uri")
        val mimeType = context.contentResolver.getType(uri)
        val extension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(mimeType)
            ?: "jpg"
        val formatter = SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US)
        val timeStamp = formatter.format(Date())

        val fileName = "IMG_${timeStamp}.${extension}"
        Log.d(TAG, "createImageFileName() returned: $fileName")
        return fileName
    }

    fun getFileFromUri(context: Context,uri: Uri): File {
        val file = createTempFile()
        context.contentResolver.openInputStream(uri).use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
        }
        Log.d(TAG, "ProfileScreen: file $file")
        return file.toFile()
    }
}