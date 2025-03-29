package com.balazs.otpflickertask.graph

import kotlinx.serialization.Serializable

@Serializable
data object MainGraph

@Serializable
data object MainScreen

@Serializable
data class PictureDetailScreen(
    val id: String,
    val secret: String,
    val url: String,
)