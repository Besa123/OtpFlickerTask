package com.balazs.otpflickertask.data.mapper

import com.balazs.otpflickertask.data.dto.PhotoSearchResponseDto
import com.balazs.otpflickertask.data.remote.dto.PhotoInfoResponseDto
import com.balazs.otpflickertask.domain.model.PhotoInfo
import com.balazs.otpflickertask.domain.model.Pictures
import com.balazs.otpflickertask.domain.model.Pictures.Picture

fun PhotoSearchResponseDto.toPictures(): Pictures {
    return Pictures(
        page = photos?.page,
        pageSize = photos?.perpage,
        totalPageAmount = photos?.pages,
        pictureUrlList = photos?.photo?.mapNotNull {
            val id = it?.id ?: return@mapNotNull null
            val secret = it.secret ?: return@mapNotNull null
            val urls = it.toPhotoUrl()

            Picture(
                id = id,
                url = urls,
                secret = secret,
            )
        } ?: listOf()
    )
}

private fun PhotoSearchResponseDto.Photos.Photo.toPhotoUrl() =
    "https://live.staticflickr.com/$server/${id}_$secret.jpg"

fun PhotoInfoResponseDto.toPhotoInfo(): PhotoInfo? {
    val photo = photo ?: return null
    return with(photo) {
        PhotoInfo(
            title = title?.content,
            description = description?.content,
            userName = owner?.username,
            realName = owner?.realname,
            photoTaken = dates?.taken,
            photoPageUrl = urls?.url?.firstOrNull { it.type == "photopage" }?.content
        )
    }
}

