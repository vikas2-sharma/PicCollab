package com.app.open.piccollab.core.db.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")
val EXPIRES_IN_KEY = longPreferencesKey("expires_in_key")



class DataStorePref(private val context: Context) {
    fun getAccessToken(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY] ?: ""
        }
    }

    suspend fun setAccessToken(accessToken: String) {
        context.dataStore.edit { settings ->
            settings[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    fun getExpiresIn(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[EXPIRES_IN_KEY] ?: 0L
        }
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
    fun getDataValue(preferencesKey: Preferences.Key<String>): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[preferencesKey] ?: ""
        }
    }
}
