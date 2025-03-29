package com.balazs.otpflickertask.domain.usecase

import com.balazs.otpflickertask.domain.repository.PictureRepository
import javax.inject.Inject

class GetPagingPicturesUseCase @Inject constructor(
    private val pictureRepository: PictureRepository,
) {
    suspend operator fun invoke(searchTerm: String) =
        pictureRepository.getPictures(searchTerm = searchTerm)
}