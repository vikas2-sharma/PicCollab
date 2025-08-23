package com.app.open.piccollab.presentation.ui.login

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.open.piccollab.R
import com.app.open.piccollab.core.auth.AuthManager
import com.app.open.piccollab.presentation.ui.login.viewmodel.LoginViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    context:Context ,
    modifier: Modifier = Modifier,
    viewmodel: LoginViewmodel = viewModel()

    ) {
    val coroutineScope = rememberCoroutineScope { Dispatchers.IO }
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        ) {
            val modifierInternal = Modifier.padding(20.dp, 10.dp)
            Column (modifier = Modifier.fillMaxWidth()) {
                /*TextField(
                    value = "",
                    onValueChange = { value -> Log.d(TAG, "username: $value") },
                    label = { Text("User Name") },
                    modifier = modifierInternal.fillMaxWidth()
                )
                TextField(
                    value = "",
                    onValueChange = { value -> Log.d(TAG, "password: $value") },
                    label = { Text("Password") },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.outline_toggle_off_24),
                            contentDescription = "password visible toggle",
                        )
                    },
                    modifier = modifierInternal.fillMaxWidth()
                )*/

                /*ButtonWithText(
                    text = "Login",
                    modifier = modifierInternal.align(Alignment.CenterHorizontally)
                ) {
                    Log.d(TAG, "LoginScreen: login button")
                }
*/
                ButtonWithText(
                    text = "Login with Google",
                    modifier = modifierInternal.align(Alignment.CenterHorizontally)
                ) {
                    Log.d(TAG, "LoginScreen: login button")
                    viewmodel.startSigning(context)
                }

            }
        }
    }
}

@Composable
fun ButtonWithText(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(20.dp, 5.dp)
    ) {

        Text(
            text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(15.dp, 0.dp)
        )
    }
}
