package com.balazs.otpflickertask.domain.repository

import kotlinx.coroutines.flow.Flow

interface SearchTermRepository {
    suspend fun saveSearchTerm(searchTerm: String): Result<String?>
    fun getSearchTerm(): Flow<Result<String?>>
}