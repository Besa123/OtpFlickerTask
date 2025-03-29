package com.balazs.otpflickertask.data.remote

import com.balazs.otpflickertask.BuildConfig
import com.balazs.otpflickertask.data.dto.PhotoSearchResponseDto
import com.balazs.otpflickertask.data.mapper.toPhotoInfo
import com.balazs.otpflickertask.data.mapper.toPictures
import com.balazs.otpflickertask.data.remote.dto.PhotoInfoResponseDto
import com.balazs.otpflickertask.data.util.get
import com.balazs.otpflickertask.domain.model.PhotoInfo
import com.balazs.otpflickertask.domain.model.Pictures
import io.ktor.client.HttpClient
import javax.inject.Inject

class FlickerApi @Inject constructor(
    private val httpClient: HttpClient,
) {
    suspend fun getPictures(
        searchTerm: String,
        page: Int,
        pageSize: Int
    ): Result<Pictures> {
        return httpClient.get<PhotoSearchResponseDto>(
            route = REST,
            queryParameters = mapOf(
                "method" to "flickr.photos.search",
                "format" to "json",
                "nojsoncallback" to 1,
                "api_key" to BuildConfig.API_KEY,
                "text" to searchTerm,
                "per_page" to pageSize,
                "page" to page
            )
        ).map { it.toPictures() }
    }

    suspend fun getPhotoInfo(
        photoId: String,
        secret: String,
    ): Result<PhotoInfo?> {
        return httpClient.get<PhotoInfoResponseDto>(
            route = REST,
            queryParameters = mapOf(
                "method" to "flickr.photos.getInfo",
                "format" to "json",
                "nojsoncallback" to 1,
                "api_key" to BuildConfig.API_KEY,
                "photo_id" to photoId,
                "secret" to secret,
            )
        ).map { it.toPhotoInfo() }
    }

    private companion object {
        const val REST = "/services/rest"
    }
}