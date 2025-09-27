package com.app.open.piccollab.core.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.open.piccollab.core.db.room.entities.EventFolder


@Dao
interface EventFolderDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEventFolder(eventFolder: EventFolder)

    @Query("SELECT * FROM events")
    suspend fun getAllEventFolder(): List<EventFolder>?

    @Query("SELECT * FROM events WHERE folderId= :folderId")
    suspend fun getEventFolderById(folderId: String): EventFolder?
}