package com.balazs.otpflickertask.domain.usecase

import com.balazs.otpflickertask.domain.repository.SearchTermRepository
import javax.inject.Inject

class SaveSearchTermUseCase @Inject constructor(
    private val searchTermRepository: SearchTermRepository,
) {
    suspend operator fun invoke(searchTerm: String) =
        searchTermRepository.saveSearchTerm(searchTerm = searchTerm)
}