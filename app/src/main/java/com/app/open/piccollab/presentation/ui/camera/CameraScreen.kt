package com.app.open.piccollab.presentation.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
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
    }
}

@Composable
fun ShowCameraPreview(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Camera")
    }
}