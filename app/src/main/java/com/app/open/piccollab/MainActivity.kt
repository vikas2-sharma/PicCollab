package com.app.open.piccollab

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.open.piccollab.presentation.ui.login.LoginScreen
import com.app.open.piccollab.presentation.ui.login.viewmodel.LoginViewmodel
import com.app.open.piccollab.presentation.ui.profile.ProfileScreen
import com.app.open.piccollab.ui.theme.PicCollabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PicCollabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    MainScreen(
                        context = context,
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    context: Context,
    name: String,
    modifier: Modifier = Modifier,
    viewmodel: LoginViewmodel = viewModel<LoginViewmodel>()
) {
    /*check success state*/
    val isLoginSuccessful = viewmodel.isSigningIn.collectAsStateWithLifecycle()
    if (isLoginSuccessful.value) {
        /*login success */
        ProfileScreen(modifier)
    } else {
        /*login not yet*/
        LoginScreen(context, modifier = modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PicCollabTheme {
//        MainScreen("Android")
    }
}