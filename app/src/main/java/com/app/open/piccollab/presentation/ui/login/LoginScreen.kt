package com.app.open.piccollab.presentation.ui.login

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.open.piccollab.presentation.common.ButtonWithText
import com.app.open.piccollab.presentation.ui.login.viewmodel.LoginViewmodel
import kotlinx.coroutines.Dispatchers

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    context: Context,
    modifier: Modifier = Modifier,
    viewmodel: LoginViewmodel = hiltViewModel(),) {
    rememberCoroutineScope { Dispatchers.IO }

    val isSigned by viewmodel.isSigningIn.collectAsState()


    val driveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "ProfileScreen: onDrivePermissionGranted")
            /*viewModel.onDrivePermissionGranted()*/
        } else {
            /*viewModel.onDrivePermissionDenied()*/
            Log.d(TAG, "ProfileScreen: onDrivePermissionDenied")
        }
    }

    val activity = LocalContext.current as Activity
    LaunchedEffect(isSigned) {
        if (isSigned) {
            Log.d(TAG, "LoginScreen: isSigned")
            viewmodel.startDrivePermission(activity) { pendingIntent ->
                val request = IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                driveLauncher.launch(request)
            }
        }
    }


    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        ) {
            val modifierInternal = Modifier.padding(20.dp, 10.dp)
            Column(modifier = Modifier.fillMaxWidth()) {
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


