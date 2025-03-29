package com.balazs.otpflickertask.data.repository

import com.balazs.otpflickertask.data.local.OtpSharedPreferencesStorage
import com.balazs.otpflickertask.domain.repository.SearchTermRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTermRepository @Inject constructor(
    private val otpSharedPreferencesStorage: OtpSharedPreferencesStorage,
) : SearchTermRepository {
    override suspend fun saveSearchTerm(searchTerm: String): Result<String?> {
        return otpSharedPreferencesStorage.setString(
            key = SEARCH_TERM,
            value = searchTerm
        )
    }

    override fun getSearchTerm(): Flow<Result<String?>> {
        return otpSharedPreferencesStorage.getString(SEARCH_TERM)
    }

    private companion object {
        const val SEARCH_TERM = "SEARCH_TERM"
    }
}