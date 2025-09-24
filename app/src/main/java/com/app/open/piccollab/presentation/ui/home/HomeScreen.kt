package com.app.open.piccollab.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.open.piccollab.presentation.common.CreateNewEventCard
import com.app.open.piccollab.presentation.common.EventFolderCard

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    modifier: Modifier,
    setFabOnClick: ((() -> Unit)?) -> Unit,
    viewmodel: HomeViewmodel = hiltViewModel()
) {
    var showNewFolderDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        setFabOnClick { showNewFolderDialog = !showNewFolderDialog }
    }
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.padding(12.dp)
    ) {

        if (showNewFolderDialog) {
            CreateNewEventCard(onCancel = { showNewFolderDialog = false }) { newEventItem ->
                val folderName = newEventItem.eventName
                Log.d(TAG, "HomeScreen: folderName: $folderName")
                viewmodel.createNewEvent(newEventItem)
                showNewFolderDialog = false
            }
        }


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