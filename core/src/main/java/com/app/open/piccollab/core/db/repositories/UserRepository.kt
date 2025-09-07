package com.app.open.piccollab.core.db.repositories

import android.util.Log
import com.app.open.piccollab.core.db.dao.UserDao
import com.app.open.piccollab.core.db.entities.User
import javax.inject.Inject

private const val TAG = "UserRepository"
class UserRepository(private val userDao: UserDao) {
    suspend fun getUserLoginDetails(): User? {
        val userDetails = userDao.getUserById()
        Log.d(TAG, "getUserLoginDetails: userDetails: $userDetails")
        return userDetails
    }

    suspend fun setUserLoginDetails(user: User){
        Log.d(TAG, "setUserLoginDetails() called with: user = $user")
        userDao.insertUser(user)
    }
}