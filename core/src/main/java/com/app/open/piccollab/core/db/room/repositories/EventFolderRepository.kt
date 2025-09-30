package com.app.open.piccollab.core.db.room.repositories

import android.util.Log
import com.app.open.piccollab.core.db.datastore.DataStorePref
import com.app.open.piccollab.core.db.datastore.ROOT_FOLDER_KEY
import com.app.open.piccollab.core.db.room.dao.EventFolderDao
import com.app.open.piccollab.core.db.room.entities.EventFolder
import com.app.open.piccollab.core.models.event.NewEventItem
import com.app.open.piccollab.core.network.module.drive.DriveManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


private const val TAG = "EventFolderRepository"
const val DEFAULT_PROJECT_FOLDER_NAME = "PicCollab_root"
const val DEFAULT_PROJECT_FOLDER_DESCRIPTION =
    "PicCollab root folder for storing required data related to project"


class EventFolderRepository(
    private val eventFolderDao: EventFolderDao,
    private val driveManager: DriveManager,
    private val dataStorePref: DataStorePref,
) {

    suspend fun getOrCreateProjectFolder(): String {
        /*getting root folder id from local data store*/
        Log.d(TAG, "getOrCreateProjectFolder: getting root folder id from local data store")
        val rootFolderIfFromPref = dataStorePref.getDataValue(ROOT_FOLDER_KEY)
        if (rootFolderIfFromPref.isNullOrBlank()) {
            /*getting root folder if from google drive*/
            Log.d(TAG, "getOrCreateProjectFolder: getting root folder if from google drive")
            val rootFolderId = driveManager.rootFolderId()
            if (rootFolderId != null) {
                Log.d(TAG, "getOrCreateProjectFolder: from drive")
                return rootFolderId
            } else {
                /*creating new root folder to google drive*/
                Log.d(TAG, "getOrCreateProjectFolder: creating new root folder to google drive")
                val rootFolderItem = NewEventItem(
                    eventName = DEFAULT_PROJECT_FOLDER_NAME,
                    eventDescription = DEFAULT_PROJECT_FOLDER_DESCRIPTION
                )
                val rootFolderIdNew = driveManager.createFolder(rootFolderItem)
                dataStorePref.setDataValue(ROOT_FOLDER_KEY, rootFolderIdNew)
                Log.d(TAG, "getOrCreateProjectFolder: from created new")
                return rootFolderIdNew
            }
        } else {
            Log.d(TAG, "getOrCreateProjectFolder: from data store")
            return rootFolderIfFromPref
        }
    }


    suspend fun createNewEventFolder(newEventItem: NewEventItem) {
        val projectFolderId = getOrCreateProjectFolder()
        val folderId = driveManager.createFolder(newEventItem, projectFolderId)

        val eventFolder = EventFolder(
            folderId = folderId,
            folderName = newEventItem.eventName,
            folderDescription = newEventItem.eventDescription
        )
        eventFolderDao.insertEventFolder(eventFolder)
    }

    fun getAllEventFolder(viewModelScope: CoroutineScope): Flow<List<EventFolder>> {
        val allEventFolder = eventFolderDao.getAllEventFolder()
        viewModelScope.launch(Dispatchers.IO) {
            val rootProjectId = getOrCreateProjectFolder()
            val eventFolderList = driveManager.getEventFolderFromDrive(rootProjectId)
            eventFolderList.forEach { eventItem ->
                eventFolderDao.insertEventFolder(eventItem)
            }
        }
        return allEventFolder
    }

    suspend fun removeAllFolderFromDB() {
        eventFolderDao.deleteAllFolder()
    }
}