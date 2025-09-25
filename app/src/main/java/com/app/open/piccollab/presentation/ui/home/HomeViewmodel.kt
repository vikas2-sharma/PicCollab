package com.app.open.piccollab.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.open.piccollab.core.models.event.NewEventItem
import com.app.open.piccollab.core.network.module.drive.DriveManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewmodel"

sealed class LoadingState {
    object Idle : LoadingState()
    data class Loading(val message: String?) : LoadingState()
    data class Success(val message: String?) : LoadingState()
    data class Error(val message: String?) : LoadingState()
}

@HiltViewModel
class HomeViewmodel @Inject constructor(private val driveManager: DriveManager) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> get() = _loadingState


    fun createNewEvent(eventItem: NewEventItem) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.emit(LoadingState.Loading("Creating new event folder"))
            driveManager.createFolder(eventItem)
            _loadingState.emit(LoadingState.Success("Event folder created"))
        }
    }
}