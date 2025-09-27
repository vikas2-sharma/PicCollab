package com.app.open.piccollab.core.db.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.open.piccollab.core.db.room.dao.EventFolderDao
import com.app.open.piccollab.core.db.room.dao.UserDao
import com.app.open.piccollab.core.db.room.entities.EventFolder
import com.app.open.piccollab.core.db.room.entities.User


@Database(entities = [User::class, EventFolder::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eventFolderDao(): EventFolderDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder<UserDatabase>(
                    context.applicationContext,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}