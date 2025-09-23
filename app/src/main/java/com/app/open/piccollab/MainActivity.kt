package com.app.open.piccollab

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.open.piccollab.core.db.datastore.DataStorePref
import com.app.open.piccollab.presentation.common.BottomNavigation
import com.app.open.piccollab.presentation.route.Home
import com.app.open.piccollab.presentation.route.Loading
import com.app.open.piccollab.presentation.route.Login
import com.app.open.piccollab.presentation.route.Profile
import com.app.open.piccollab.presentation.ui.home.HomeScreen
import com.app.open.piccollab.presentation.ui.login.LoginScreen
import com.app.open.piccollab.presentation.ui.profile.ProfileScreen
import com.app.open.piccollab.ui.theme.PicCollabTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val TAG = "MainActivity"


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dataStorePref: DataStorePref

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PicCollabTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: ""

                LaunchedEffect(navBackStackEntry) {
                    Log.d(TAG, "onCreate: currentDestination2: $currentRoute")
                }
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButtonByRoutes(currentRoute)
                    }, bottomBar = {
                        BottomNavigation(selectedRoute = currentRoute, navigateToHome = {
                            navController.navigate(Home) {
                                popUpTo(Home) { inclusive = true }
                            }
                        }, navigateToProfile = { navController.navigate(Profile) })
                    }, topBar = {
                        TopAppBar(
                            /*colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),*/ title = {
                                Text(text = "PicCollab", fontWeight = FontWeight.ExtraBold)
                            })
                    }, modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val context = LocalContext.current
                    MainScreen(
                        context = context,
                        modifier = Modifier.padding(innerPadding),
                        dataStorePref = dataStorePref,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingActionButtonByRoutes(currentRoute: String) {
    when (currentRoute) {
        Home::class.qualifiedName -> {
            FloatingActionButton({
                Log.d(TAG, "onCreate: fab")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }

        else -> {}
    }
}

@Composable
fun MainScreen(
    context: Context,
    modifier: Modifier = Modifier,
    dataStorePref: DataStorePref,
    navController: NavHostController
) {

    val accessToken by dataStorePref.getAccessToken().collectAsState(initial = null)
    LaunchedEffect(accessToken) {
        Log.d(TAG, "MainScreen: tokenValue: $accessToken")
    }

    NavHost(
        navController = navController,
        startDestination = when {
            accessToken == null -> Loading
            !accessToken.isNullOrEmpty() -> Home
            else -> Login
        },
    ) {
        composable<Login> {
            LoginScreen(
                context, modifier = modifier,
            )
        }
        composable<Profile> {
            ProfileScreen(modifier)
        }
        composable<Home> {
            HomeScreen(modifier = modifier)
        }
        composable<Loading> {
            LoadingScreen()
        }
    }
}


@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Loading...")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PicCollabTheme {
//        MainScreen()
    }
}