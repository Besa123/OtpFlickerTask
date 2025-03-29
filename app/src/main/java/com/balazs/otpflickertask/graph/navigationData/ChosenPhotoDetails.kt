package com.balazs.otpflickertask.graph.navigationData

data class ChosenPhotoDetails(
    val photoId: String,
    val secret: String,
    val photoUrl: String,
)
