package com.app.open.piccollab.presentation.ui.login.viewmodel

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.open.piccollab.core.auth.AuthManager
import com.app.open.piccollab.core.db.room.entities.User
import com.app.open.piccollab.core.db.room.repositories.UserRepository
import com.app.open.piccollab.core.models.user.UserData
import com.app.open.piccollab.core.network.module.RestApiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewmodel"

@HiltViewModel
class LoginViewmodel @Inject constructor(
    private val restApiManager: RestApiManager,
    private val authManager: AuthManager,
    private val userRepository: UserRepository
) : ViewModel() {
    private val signingState = MutableStateFlow(false)
    val isSigningIn = signingState.asStateFlow()

    private val _userDataFlow = MutableStateFlow<UserData?>(null)
    val userDataFlow = _userDataFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val userDetails = userRepository.getUserLoginDetails()
            if (userDetails != null) {
                _userDataFlow.value = UserData(
                    profilePictureUri = userDetails.profilePictureUri.toUri(),
                    displayName = userDetails.displayName,
                    phoneNumber = userDetails.phoneNumber,
                    id = userDetails.id,
                    givenName = userDetails.givenName,
                    idToken = userDetails.idToken,
                    familyName = userDetails.familyName,
                )
            }

        }
    }

    fun startSigning(context: Context) {
        viewModelScope.launch {
            authManager.startGoogleAuthentication(context).collect { userData ->
                Log.d(TAG, "startSigning: UserData : $userData")
                if (userData == null || userData.id?.isEmpty() == true) {
                    signingState.value = false
                } else {
                    signingState.value = true
                    _userDataFlow.value = userData
                    userRepository.setUserLoginDetails(
                        User(
                            profilePictureUri = userData.profilePictureUri.toString(),
                            displayName = userData.displayName ?: "",
                            phoneNumber = userData.phoneNumber ?: "",
                            id = userData.id ?: "",
                            givenName = userData.givenName ?: "",
                            idToken = userData.idToken ?: "",
                            familyName = userData.familyName ?: "",
                        )
                    )
                }
            }
        }
    }

    fun startDrivePermission(activity: Activity, launchIntent: (PendingIntent) -> Unit) {
        Log.d(
            TAG,
            "startDrivePermission() called with: activity = $activity, launchIntent = $launchIntent"
        )
        viewModelScope.launch {
            authManager.getDrivePermission(
                activity = activity, launchIntent = launchIntent
            )
        }
    }

    fun getUserDriveDetail() {
        viewModelScope.launch {
            val queryMap = HashMap<String, String>()
            queryMap.put("fields", "user,storageQuota")
            try {
                val response = restApiManager.getUserDetails(queryMap)
                Log.d(TAG, "getUserDriveDetail: $response")
            } catch (e: Exception) {
                Log.e(TAG, "getUserDriveDetail: ", e)
            }
        }
    }
}
