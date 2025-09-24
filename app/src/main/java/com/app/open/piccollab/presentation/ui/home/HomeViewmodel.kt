package com.app.open.piccollab.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.open.piccollab.core.models.event.NewEventItem
import com.app.open.piccollab.core.network.module.drive.DriveManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewmodel"

@HiltViewModel
class HomeViewmodel @Inject constructor(private val driveManager: DriveManager) : ViewModel() {
    fun createNewEvent(eventItem: NewEventItem) {
        viewModelScope.launch(Dispatchers.IO) {
            driveManager.createFolder(eventItem)
        }
    }
}