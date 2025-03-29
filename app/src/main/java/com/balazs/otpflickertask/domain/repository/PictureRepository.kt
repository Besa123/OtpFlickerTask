package com.balazs.otpflickertask.domain.repository

import androidx.paging.PagingData
import com.balazs.otpflickertask.domain.model.PhotoInfo
import com.balazs.otpflickertask.domain.model.Pictures.Picture
import kotlinx.coroutines.flow.Flow

interface PictureRepository {
    suspend fun getPictures(searchTerm: String): Flow<PagingData<Picture>>
    suspend fun getPhotoInfo(
        photoId: String,
        secret: String,
    ): Result<PhotoInfo?>
}