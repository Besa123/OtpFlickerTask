package com.balazs.otpflickertask.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.balazs.otpflickertask.data.paging.FlickrPicturePaging
import com.balazs.otpflickertask.data.paging.FlickrPicturePaging.Companion.PAGE_SIZE
import com.balazs.otpflickertask.data.remote.FlickerApi
import com.balazs.otpflickertask.domain.model.Pictures.Picture
import com.balazs.otpflickertask.domain.repository.PictureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FlickrPictureRepository @Inject constructor(
    private val flickerApi: FlickerApi,
) : PictureRepository {
    override suspend fun getPictures(
        searchTerm: String,
    ): Flow<PagingData<Picture>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                FlickrPicturePaging(
                    flickerApi = flickerApi,
                    searchTerm = searchTerm,
                )
            }
        ).flow
    }

    override suspend fun getPhotoInfo(photoId: String, secret: String) = flickerApi.getPhotoInfo(
        photoId = photoId,
        secret = secret
    )
}