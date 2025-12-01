package com.app.open.piccollab.presentation.ui.camera

import android.Manifest
import android.util.Log
import androidx.camera.compose.CameraXViewfinder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.open.piccollab.presentation.common.ButtonWithText
import com.app.open.piccollab.presentation.common.SpacerHeight
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

private const val TAG = "CameraScreen"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    Log.d(TAG, "CameraScreen: camera permission state: ${cameraPermissionState.status.isGranted}")
    if (cameraPermissionState.status.isGranted) {
        ShowCameraPreview(modifier)
    } else {
        Log.d(TAG, "CameraScreen: get permission request")
        ShowCameraPermissionRequest(cameraPermissionState, modifier)
    }
}

@Composable
fun ShowCameraPreview(modifier: Modifier, viewmodel: CameraViewmodel = viewModel()) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val surfaceRequest by viewmodel.surfaceRequest.collectAsStateWithLifecycle()
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current

        LaunchedEffect(lifecycleOwner) {
            viewmodel.bindToCamera(context.applicationContext, lifecycleOwner)
        }

        surfaceRequest?.let { request ->
            CameraXViewfinder(
                surfaceRequest = request,
            )
        }

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowCameraPermissionRequest(cameraPermissionState: PermissionState, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Provide camera permission to scan the QR codes")

        SpacerHeight(8.dp)
        ButtonWithText("Camera Permission") {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}