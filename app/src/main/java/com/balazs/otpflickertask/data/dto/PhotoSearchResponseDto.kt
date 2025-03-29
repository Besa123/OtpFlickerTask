package com.balazs.otpflickertask.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoSearchResponseDto(
    @SerialName("photos")
    val photos: Photos? = null,
    @SerialName("stat")
    val stat: String? = null
) {
    @Serializable
    data class Photos(
        @SerialName("page")
        val page: Int? = null,
        @SerialName("pages")
        val pages: Int? = null,
        @SerialName("perpage")
        val perpage: Int? = null,
        @SerialName("photo")
        val photo: List<Photo?>? = null,
        @SerialName("total")
        val total: Int? = null
    ) {
        @Serializable
        data class Photo(
            @SerialName("farm")
            val farm: Int? = null,
            @SerialName("id")
            val id: String? = null,
            @SerialName("isfamily")
            val isfamily: Int? = null,
            @SerialName("isfriend")
            val isfriend: Int? = null,
            @SerialName("ispublic")
            val ispublic: Int? = null,
            @SerialName("owner")
            val owner: String? = null,
            @SerialName("secret")
            val secret: String? = null,
            @SerialName("server")
            val server: String? = null,
            @SerialName("title")
            val title: String? = null
        )
    }
}