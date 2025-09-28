package com.app.open.piccollab.presentation.ui.profile

import android.app.Activity
import android.util.Printer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.open.piccollab.core.auth.AuthManager
import com.app.open.piccollab.core.db.datastore.DataStorePref
import com.app.open.piccollab.core.db.room.repositories.EventFolderRepository
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
    private val authManager: AuthManager,
    private val driveManager: DriveManager,
    private val eventFolderRepository: EventFolderRepository
) : ViewModel() {


    val tokenState = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePref.getAccessToken().collect { value ->
                tokenState.value = value
            }
        }
    }

    fun logout(activity: Activity){
        viewModelScope.launch {
            dataStorePref.clearAllData()
            userRepository.removeAllUser()
            authManager.logout(activity = activity)
            eventFolderRepository.removeAllFolderFromDB()
        }
    }

}