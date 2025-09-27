package com.app.open.piccollab.core.db.room.repositories

import com.app.open.piccollab.core.db.room.dao.EventFolderDao
import com.app.open.piccollab.core.db.room.entities.EventFolder
import com.app.open.piccollab.core.models.event.NewEventItem

class EventFolderRepository(private val eventFolderDao: EventFolderDao) {
    suspend fun createNewFolder(eventFolder: EventFolder) {
        eventFolderDao.insertEventFolder(eventFolder)
    }
    suspend fun getAllEventFolder(): List<EventFolder>{
        return eventFolderDao.getAllEventFolder()?:emptyList()
    }
}