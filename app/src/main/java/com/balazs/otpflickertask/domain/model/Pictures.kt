package com.balazs.otpflickertask.domain.model

data class Pictures(
    val pictureUrlList: List<Picture>?,
    val totalPageAmount: Int?,
    val page: Int?,
    val pageSize: Int?,
) {
    data class Picture(
        val id: String,
        val url: String,
        val secret: String,
    )
}
