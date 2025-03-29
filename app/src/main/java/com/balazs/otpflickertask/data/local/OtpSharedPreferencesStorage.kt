package com.balazs.otpflickertask.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OtpSharedPreferencesStorage @Inject constructor(
    val dataSore: DataStore<Preferences>,
) {
    fun getString(
        key: String
    ): Flow<Result<String?>> {
        return getDataStore(key = stringPreferencesKey(name = key))
    }

    suspend fun setString(
        key: String,
        value: String,
    ): Result<String?> {
        return setDataStore(
            key = stringPreferencesKey(key),
            value = value
        )
    }

    private fun <T> getDataStore(
        key: Preferences.Key<T>,
    ): Flow<Result<T?>> {
        return dataSore
            .data
            .catch { emit(emptyPreferences()) }
            .map { preferences ->
                safeCallDataStore {
                    preferences[key]
                }
            }
    }

    private suspend fun <T> setDataStore(
        key: Preferences.Key<T>,
        value: T,
    ): Result<T?> {
        return withContext(Dispatchers.IO) {
            safeCallDataStore {
                dataSore
                    .edit { preferences ->
                        preferences[key] = value
                    }[key]
            }
        }
    }
}