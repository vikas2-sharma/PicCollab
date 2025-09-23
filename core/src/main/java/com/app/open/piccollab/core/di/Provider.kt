package com.app.open.piccollab.core.di

import android.content.Context
import com.app.open.piccollab.core.db.datastore.DataStorePref
import com.app.open.piccollab.core.db.room.database.UserDatabase
import com.app.open.piccollab.core.network.module.drive.DriveManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Provider {
    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) = UserDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun providesUserDao(userDatabase: UserDatabase) = userDatabase.userDao()

    @Singleton
    @Provides
    fun providesDataStore(@ApplicationContext context: Context) = DataStorePref(context)

    @Singleton
    @Provides
    fun providesDriveManager(@ApplicationContext context: Context, dataStorePref: DataStorePref) =
        DriveManager(context, dataStorePref)


}