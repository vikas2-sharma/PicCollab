package com.app.open.piccollab

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.open.piccollab.presentation.common.BottomNavigation
import com.app.open.piccollab.presentation.ui.login.LoginScreen
import com.app.open.piccollab.presentation.ui.login.viewmodel.LoginViewmodel
import com.app.open.piccollab.presentation.ui.profile.ProfileScreen
import com.app.open.piccollab.ui.theme.PicCollabTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PicCollabTheme {
                Scaffold(
                    bottomBar = {
                        BottomNavigation()
                    },
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(text = "PicCollab")
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val context = LocalContext.current
                    MainScreen(
                        context = context,
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

        /*

         {
      "access_token": "09eut09ru",
      "refresh_token_expires_in": 604799,
      "expires_in": 3599,
      "token_type": "Bearer",
      "scope": "https://www.googleapis.com/auth/drive",
      "refresh_token": "9urt09"
    }


        * */
    }
}