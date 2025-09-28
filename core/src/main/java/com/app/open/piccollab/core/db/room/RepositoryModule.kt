package com.app.open.piccollab.core.db.room

import com.app.open.piccollab.core.db.datastore.DataStorePref
import com.app.open.piccollab.core.db.room.dao.EventFolderDao
import com.app.open.piccollab.core.db.room.dao.UserDao
import com.app.open.piccollab.core.db.room.repositories.EventFolderRepository
import com.app.open.piccollab.core.db.room.repositories.UserRepository
import com.app.open.piccollab.core.network.module.drive.DriveManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun providesUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)

    }

    @Singleton
    @Provides
    fun providesEventFolderRepository(
        eventFolderDao: EventFolderDao,
        driveManager: DriveManager,
        dataStorePref: DataStorePref
    ): EventFolderRepository {
        return EventFolderRepository(eventFolderDao, driveManager, dataStorePref)

    }
}