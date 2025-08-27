package com.app.open.piccollab.presentation.ui.profile

import android.widget.TextView
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
import com.app.open.piccollab.presentation.ui.login.viewmodel.LoginViewmodel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier, viewModel: LoginViewmodel = viewModel<LoginViewmodel>()
) {
    Box(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

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

            /* Image(
                 painter = painterResource(R.drawable.ic_launcher_background),
                 contentDescription = "he",
                 modifier = Modifier

             )*/
            Column(modifier = Modifier.fillMaxWidth()) {
                val textModifier = Modifier
                    .align(Alignment.Start)
                    .padding(0.dp, 4.dp)
                HtmlText(html = "<b>${userData?.displayName}</b>", modifier = textModifier)
                HtmlText(html = "Email:<b><i> ${userData?.id}</i></b>", modifier = textModifier)
            }
        }
    }
}


@Composable
fun HtmlText(
    html: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        },
        update = {
            it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProfileScreen()
}