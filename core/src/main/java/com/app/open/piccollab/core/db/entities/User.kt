package com.app.open.piccollab.core.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")
data class User(
    val profilePictureUri: String,
    val displayName: String,
    val phoneNumber: String,
    @PrimaryKey val id: String,
    val givenName: String,
    val idToken: String,
    val familyName: String,
)
