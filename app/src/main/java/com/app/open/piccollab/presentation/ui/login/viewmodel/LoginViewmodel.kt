package com.app.open.piccollab.presentation.ui.login.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.open.piccollab.core.auth.AuthManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewmodel : ViewModel() {
    private val signingState = MutableStateFlow(false)
    val isSigningIn = signingState.asStateFlow()


    fun startSigning(context: Context) {
        viewModelScope.launch {
            AuthManager.startGoogleAuthentication(context)
                .collect { result -> signingState.value = result }
        }
    }
}
