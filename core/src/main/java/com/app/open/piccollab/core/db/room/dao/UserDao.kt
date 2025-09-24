package com.app.open.piccollab.core.db.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.open.piccollab.core.db.room.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user")
    suspend fun getUserById(): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM user")
    suspend fun removeUser()


}