package com.app.open.piccollab.core.di

import android.content.Context
import com.app.open.piccollab.core.db.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.coroutines.Continuation

@Module
@InstallIn(SingletonComponent::class)
class Provider {
    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) = UserDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun providesUserDao(userDatabase: UserDatabase) = userDatabase.userDao()

}