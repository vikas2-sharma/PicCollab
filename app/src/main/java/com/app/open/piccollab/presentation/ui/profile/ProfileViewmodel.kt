package com.app.open.piccollab.presentation.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.open.piccollab.core.db.datastore.DataStorePref
import com.app.open.piccollab.core.db.room.repositories.UserRepository
import com.app.open.piccollab.core.network.module.drive.DriveManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProfileViewmodel"

@HiltViewModel
class ProfileViewmodel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStorePref: DataStorePref,
    private val driveManager: DriveManager
) : ViewModel() {


    val tokenState = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePref.getAccessToken().collect { value ->
                tokenState.value = value
            }
        }
    }

    fun createFolder(folderName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val folderId = driveManager.createFolder(folderName)
            Log.d(TAG, "createFolder: $folderId")
        }
    }

}