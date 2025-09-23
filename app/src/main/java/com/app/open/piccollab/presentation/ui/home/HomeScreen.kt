package com.app.open.piccollab.presentation.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.open.piccollab.presentation.common.EventFolderCard

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(modifier: Modifier) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
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

@Preview(showSystemUi = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(Modifier)
}