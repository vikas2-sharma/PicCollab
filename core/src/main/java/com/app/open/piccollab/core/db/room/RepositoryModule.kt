package com.app.open.piccollab.core.db.room

import com.app.open.piccollab.core.db.room.dao.UserDao
import com.app.open.piccollab.core.db.room.repositories.UserRepository
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
}