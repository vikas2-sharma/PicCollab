package com.app.open.piccollab.core.db.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "events")
data class EventFolder(
    @PrimaryKey
    val folderId: String,
    val folderName: String,
    val folderDescription: String,

    )