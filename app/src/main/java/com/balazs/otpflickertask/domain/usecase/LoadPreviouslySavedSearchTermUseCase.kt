package com.balazs.otpflickertask.domain.usecase

import com.balazs.otpflickertask.domain.repository.SearchTermRepository
import javax.inject.Inject

class LoadPreviouslySavedSearchTermUseCase @Inject constructor(
    private val searchTermRepository: SearchTermRepository,
) {
    operator fun invoke() = searchTermRepository.getSearchTerm()
}