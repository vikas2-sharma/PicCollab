package com.app.open.piccollab.core.db.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")
private const val TAG = "DataStorePref"

val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")
val EXPIRES_IN_KEY = longPreferencesKey("expires_in_key")
val ROOT_FOLDER_KEY = stringPreferencesKey("root.folder.key")


class DataStorePref(private val context: Context) {
    fun getAccessToken(): Flow<String> {
        Log.d(TAG, "getAccessToken() called")
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY] ?: ""
        }
    }

    suspend fun setAccessToken(accessToken: String) {
        Log.d(TAG, "setAccessToken() called with: accessToken = $accessToken")
        context.dataStore.edit { settings ->
            settings[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    suspend fun getExpiresIn(): Long {
        return context.dataStore.data.map { preferences ->
            preferences[EXPIRES_IN_KEY] ?: 0L
        }.first()
    }

    suspend fun setExpiresIn(expiresIn: Long) {
        context.dataStore.edit { settings ->
            settings[EXPIRES_IN_KEY] = expiresIn
        }
    }

    suspend fun setDataValue(preferencesKey: Preferences.Key<String>, value: String) {
        context.dataStore.edit { settings ->
            settings[preferencesKey] = value
        }
    }

    fun getDataValueFlow(preferencesKey: Preferences.Key<String>): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[preferencesKey] ?: ""
        }
    }

    suspend fun getDataValue(preferencesKey: Preferences.Key<String>): String? {
        return context.dataStore.data.map { preferences ->
            preferences[preferencesKey]
        }.first()
    }

    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
