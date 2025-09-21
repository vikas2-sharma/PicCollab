package com.app.open.piccollab.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.open.piccollab.R
import com.app.open.piccollab.presentation.common.EventFolderCard

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(modifier: Modifier) {
    Scaffold (/*floatingActionButton = {
        FloatingActionButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Dsds"
            )
        }
    }*/){ innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = modifier.padding(innerPadding)
        ) {

            val eventList = listOf(
                "Event A",
                "Event B",
                "Event C",
                "Event D",
            )
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
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(Modifier)
}