package com.balazs.otpflickertask.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

const val OTP_PREFERENCES_DATA_STORE = "OTP_PREFERENCES_DATA_STORE"

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = OTP_PREFERENCES_DATA_STORE
)

inline fun <T> safeCallDataStore(execute: () -> T): Result<T> {
    return try {
        Result.success(execute())
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}