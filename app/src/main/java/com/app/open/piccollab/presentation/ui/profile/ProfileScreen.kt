package com.app.open.piccollab.presentation.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.app.open.piccollab.R
import com.app.open.piccollab.presentation.ui.login.viewmodel.LoginViewmodel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewmodel = viewModel<LoginViewmodel>()
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val userData = viewModel.userDataFlow.collectAsStateWithLifecycle().value
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userData?.profilePictureUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile picture",
                placeholder = painterResource(R.drawable.outline_person_24),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
            )
            Text(text = "Name: ${userData?.displayName}")
            Text(text = "Email: ${userData?.id}")
        }
    }
}