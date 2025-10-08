package com.app.open.piccollab.presentation.ui.home

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.open.piccollab.core.db.room.entities.EventFolder
import com.app.open.piccollab.core.utils.ImageUtil
import com.app.open.piccollab.presentation.common.CreateNewEventCard
import com.app.open.piccollab.presentation.common.EventFolderCard
import com.app.open.piccollab.presentation.common.ProgressDialog
import com.app.open.piccollab.presentation.common.ShareEventQrCard
import com.google.gson.Gson

private const val TAG = "HomeScreen"
private val gson = Gson()

@Composable
fun HomeScreen(
    modifier: Modifier,
    setFabOnClick: ((() -> Unit)?) -> Unit,
    viewmodel: HomeViewmodel = hiltViewModel()
) {
    var showNewFolderDialog by rememberSaveable { mutableStateOf(false) }
    val loadingState by viewmodel.loadingState.collectAsState()
    val eventList by viewmodel.eventFolderFlow().collectAsState(emptyList())
    var shareEventFolder by remember { mutableStateOf<EventFolder?>(null) }

    LaunchedEffect(Unit) {
        setFabOnClick { showNewFolderDialog = !showNewFolderDialog }
    }
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.padding(12.dp)
    ) {

        if (shareEventFolder != null) {
            ShareEventQrCard(
                ImageUtil.getQrBitmap(
                    gson.toJson(shareEventFolder),
                    screenWidth
                ), shareEventFolder,
                onDismiss = { shareEventFolder = null }
            ) {
                shareEventFolder = null
            }
        }

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
        Log.d(TAG, "HomeScreen: eventList size: ${eventList.size}")
        val configuration = LocalConfiguration.current
        val cellCounts = if (configuration.orientation == ORIENTATION_LANDSCAPE) 4 else 2
        LazyVerticalGrid(
            columns = GridCells.Fixed(cellCounts),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(eventList, key = { eventItem -> eventItem.folderId }) { eventItem ->
                Log.d(TAG, "HomeScreen: eventItem: ${eventItem.folderName}")
                EventFolderCard(
                    eventItem = eventItem,
                    onDeleteClick = { eventItem ->
                        Log.d(
                            TAG,
                            "HomeScreen: todo delete ${eventItem.folderName}"
                        )
                        viewmodel.deleteEventFolder(eventItem)
                    },
                    onRenameClick = { eventItem ->
                        Log.d(
                            TAG,
                            "HomeScreen: todo rename ${eventItem.folderName}"
                        )
                    },
                    onShareClick = { eventItem ->
                        Log.d(TAG, "HomeScreen: share: ${eventItem.folderName}")
                        shareEventFolder = eventItem
                    }
                ) {
                    Log.d(TAG, "HomeScreen: open folder: ${eventItem.folderName}")
                }
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHomeScreen() {
    /* HomeScreen(Modifier)*/
}