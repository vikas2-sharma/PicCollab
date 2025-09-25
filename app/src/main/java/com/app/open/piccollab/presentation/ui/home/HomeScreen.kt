package com.app.open.piccollab.presentation.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.open.piccollab.presentation.common.CreateNewEventCard
import com.app.open.piccollab.presentation.common.EventFolderCard
import com.app.open.piccollab.presentation.common.ProgressDialog

private const val TAG = "HomeScreen"

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    modifier: Modifier,
    setFabOnClick: ((() -> Unit)?) -> Unit,
    viewmodel: HomeViewmodel = hiltViewModel()
) {
    var showNewFolderDialog by remember { mutableStateOf(false) }
    val loadingState by viewmodel.loadingState.collectAsState()
    LaunchedEffect(Unit) {
        setFabOnClick { showNewFolderDialog = !showNewFolderDialog }
    }
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.padding(12.dp)
    ) {

        /*dialog*/
        if (showNewFolderDialog) {
            CreateNewEventCard(onCancel = { showNewFolderDialog = false }) { newEventItem ->
                val folderName = newEventItem.eventName
                Log.d(TAG, "HomeScreen: folderName: $folderName")
                viewmodel.createNewEvent(newEventItem)
                showNewFolderDialog = false
            }
        }


        /*loading*/
        when (loadingState) {
            is LoadingState.Loading -> {
                val message = (loadingState as LoadingState.Loading).message ?: "Loading..."
                ProgressDialog(message)
            }

            is LoadingState.Success -> {
                val message = (loadingState as LoadingState.Success).message ?: "Loading..."
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            else -> {

            }
        }


        /*main content*/
        val eventList = emptyList<String>()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(eventList) { eventName ->
                EventFolderCard(eventName = eventName)
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHomeScreen() {
    /* HomeScreen(Modifier)*/
}