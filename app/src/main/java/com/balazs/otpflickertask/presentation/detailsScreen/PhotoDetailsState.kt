package com.balazs.otpflickertask.presentation.detailsScreen

import com.balazs.otpflickertask.domain.model.PhotoInfo

data class PhotoDetailsState(
    val photoInfo: PhotoInfo? = null,
    val photoUrl: String? = null,
    val isLoading: Boolean = false
)
