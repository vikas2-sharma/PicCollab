package com.app.open.piccollab.presentation.ui.profile

import android.app.Activity
import android.util.Log
import android.widget.TextView
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.app.open.piccollab.R
import com.app.open.piccollab.presentation.common.ButtonWithText
import com.app.open.piccollab.presentation.ui.login.viewmodel.LoginViewmodel

private const val TAG = "ProfileScreen"

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier, viewModel: LoginViewmodel = viewModel<LoginViewmodel>()
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
           /* .background(Color.Blue)*/,
    ) {

        val driveLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "ProfileScreen: onDrivePermissionGranted" )
                /*viewModel.onDrivePermissionGranted()*/
            } else {
                /*viewModel.onDrivePermissionDenied()*/
                Log.d(TAG, "ProfileScreen: onDrivePermissionDenied" )
            }
        }
        UserDetailRow(modifier, viewModel)

        DriveDataPanel(viewModel, driveLauncher)

        DriveDetailButton(viewModel)

    }
}


@Composable
private fun DriveDetailButton(
    viewModel: LoginViewmodel,
) {
    ButtonWithText(
        text = "Drive Detail"
    ) {
        Log.d(TAG, "ProfileScreen: Drive Permission")
        viewModel.getUserDriveDetail()
    }
}


@Composable
private fun DriveDataPanel(
    viewModel: LoginViewmodel,
    driveLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
) {
    val activity = LocalContext.current as Activity

    ButtonWithText(
        text = "Drive Permission"
    ) {
        Log.d(TAG, "ProfileScreen: Drive Permission")
        viewModel.startDrivePermission(activity) { pendingIntent ->
            val request = IntentSenderRequest.Builder(pendingIntent.intentSender).build()
            driveLauncher.launch(request)

        }
    }
}

@Composable
private fun UserDetailRow(
    modifier: Modifier,
    viewModel: LoginViewmodel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            /*.background(Color.Red)*/
            .padding(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        UserDetailPanel(viewModel)
    }
}

@Composable
private fun UserDetailPanel(viewModel: LoginViewmodel) {
    val userData = viewModel.userDataFlow.collectAsStateWithLifecycle().value
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(userData?.profilePictureUri)
            .crossfade(true).build(),
        contentDescription = "Profile picture",
        placeholder = painterResource(R.drawable.outline_person_24),
        modifier = Modifier
            .padding(20.dp, 0.dp)
            .size(56.dp)
            .clip(CircleShape)

    )
    Column(modifier = Modifier.fillMaxWidth()) {
        val textModifier = Modifier
            .align(Alignment.Start)
            .padding(0.dp, 4.dp)
        HtmlText(html = "<b>${userData?.displayName}</b>", modifier = textModifier)
        HtmlText(html = "Email:<b><i> ${userData?.id}</i></b>", modifier = textModifier)
    }
}


@Composable
fun HtmlText(
    html: String, modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
        TextView(context).apply {
            text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }, update = {
        it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }, modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProfileScreen()
}