package com.app.open.piccollab.core.db

import com.app.open.piccollab.core.db.dao.UserDao
import com.app.open.piccollab.core.db.repositories.UserRepository
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