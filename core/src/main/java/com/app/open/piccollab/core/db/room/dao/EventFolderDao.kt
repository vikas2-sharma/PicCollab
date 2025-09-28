package com.app.open.piccollab.core.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.open.piccollab.core.db.room.entities.EventFolder
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE


@Dao
interface EventFolderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEventFolder(eventFolder: EventFolder)

    @Query("SELECT * FROM events")
    fun getAllEventFolder(): Flow<List<EventFolder>>

    @Query("SELECT * FROM events WHERE folderId= :folderId")
    suspend fun getEventFolderById(folderId: String): EventFolder?

    @Query("DELETE FROM events")
    suspend fun deleteAllFolder()
}