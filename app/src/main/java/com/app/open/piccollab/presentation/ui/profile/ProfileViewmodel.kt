package com.app.open.piccollab.presentation.ui.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.open.piccollab.core.db.datastore.DataStorePref
import com.app.open.piccollab.core.db.room.repositories.UserRepository
import com.app.open.piccollab.core.network.upload.FileUploader
import com.app.open.piccollab.core.utils.FileUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

private const val TAG = "ProfileViewmodel"

@HiltViewModel
class ProfileViewmodel @Inject constructor(
    private val userRepository: UserRepository,
    private val fileUploader: FileUploader,
    private val dataStorePref: DataStorePref
) : ViewModel() {


    val tokenState = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePref.getAccessToken().collect { value ->
                tokenState.value = value
            }
        }
    }

    fun uploadFile(context: Context, file: File) {
        Log.d(TAG, "uploadFile() called")
        val filePath = FileUtil.moveAssetToStorage(context, "")
        viewModelScope.launch(Dispatchers.IO) {
            if (filePath != null && tokenState.value != null) {
                fileUploader.fileDriveUpload(tokenState.value!!, file.path)
            } else {
                Log.d(TAG, "uploadFile: File path is null")
            }
        }
    }

    fun uploadFile(context: Context, uri: Uri) {
        Log.d(TAG, "uploadFile() called")
        val filePath = FileUtil.moveAssetToStorage(context, "")
        viewModelScope.launch(Dispatchers.IO) {
            if (filePath != null && tokenState.value != null) {
                fileUploader.uploadFileUsingUri(tokenState.value!!, uri)
            } else {
                Log.d(TAG, "uploadFile: File path is null")
            }
        }
    }
}