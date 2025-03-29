package com.balazs.otpflickertask.domain.usecase

import com.balazs.otpflickertask.domain.repository.PictureRepository
import javax.inject.Inject

class GetPictureInformationUseCase @Inject constructor(
    private val pictureRepository: PictureRepository,
) {
    suspend operator fun invoke(
        photoId: String,
        secret: String,
    ) = pictureRepository.getPhotoInfo(
        photoId = photoId,
        secret = secret
    )
}