package com.app.open.piccollab.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.open.piccollab.core.db.room.entities.EventFolder
import com.app.open.piccollab.core.db.room.repositories.EventFolderRepository
import com.app.open.piccollab.core.models.event.NewEventItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
class HomeViewmodel @Inject constructor(
    private val eventFolderRepository: EventFolderRepository
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> get() = _loadingState


    fun createNewEvent(eventItem: NewEventItem) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.emit(LoadingState.Loading("Creating new event folder"))
            eventFolderRepository.createNewEventFolder(eventItem)
            _loadingState.emit(LoadingState.Success("Event folder created"))
            eventFolderRepository.refreshEventFolderWithDrive(viewModelScope)
        }
    }

    fun setRootFolder() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.emit(LoadingState.Loading("creating root folder, Please wait"))
            val rootFolderId = eventFolderRepository.getOrCreateProjectFolder()
            Log.d(TAG, "setRootFolder: folderId: $rootFolderId")
            _loadingState.emit(LoadingState.Idle)
        }
        eventFolderRepository.refreshEventFolderWithDrive(viewModelScope)
    }

    fun eventFolderFlow(): Flow<List<EventFolder>> {
        Log.d(TAG, "eventFolderFlow: ")
        return eventFolderRepository.getAllEventFolder(viewModelScope)
    }

    fun deleteEventFolder(eventItem: EventFolder) {
        Log.d(TAG, "deleteEventFolder() called with: eventItem = $eventItem")
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.emit(LoadingState.Loading("Deleting ${eventItem.folderName}"))
            eventFolderRepository.deleteFolder(eventItem)
            _loadingState.emit(LoadingState.Idle)
        }
        eventFolderRepository.refreshEventFolderWithDrive(viewModelScope)
    }
}