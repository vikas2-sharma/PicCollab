package com.app.open.piccollab.presentation.ui.login

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.open.piccollab.presentation.common.ButtonWithText
import com.app.open.piccollab.presentation.ui.login.viewmodel.LoginViewmodel
import kotlinx.coroutines.Dispatchers

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    context:Context ,
    modifier: Modifier = Modifier,
    viewmodel: LoginViewmodel = hiltViewModel(),
    navigateToProfile : ()-> Unit

    ) {
    rememberCoroutineScope { Dispatchers.IO }
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        ) {
            val modifierInternal = Modifier.padding(20.dp, 10.dp)
            Column (modifier = Modifier.fillMaxWidth()) {
                val ss = viewmodel.isSigningIn.collectAsState()
                if (ss.value){
                    navigateToProfile()
                }
                ButtonWithText(
                    text = "Login with Google",
                    modifier = modifierInternal.align(Alignment.CenterHorizontally)
                ) {

                    Log.d(TAG, "LoginScreen: login button")
//                    navigateToProfile()
                    viewmodel.startSigning(context)
                }
            }
        }
    }
}


