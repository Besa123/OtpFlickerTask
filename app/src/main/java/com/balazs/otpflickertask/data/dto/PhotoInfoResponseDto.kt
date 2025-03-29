package com.balazs.otpflickertask.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoInfoResponseDto(
    val photo: Photo? = null,
    val stat: String? = null
) {
    @Serializable
    data class Photo(
        val id: String? = null,
        val secret: String? = null,
        val server: String? = null,
        val farm: Int? = null,
        val dateuploaded: String? = null,
        val isfavorite: Int? = null,
        val license: String? = null,
        @SerialName("safety_level") val safetyLevel: String? = null,
        val rotation: Int? = null,
        val originalsecret: String? = null,
        val originalformat: String? = null,
        val owner: Owner? = null,
        val title: Title? = null,
        val description: Description? = null,
        val visibility: Visibility? = null,
        val dates: Dates? = null,
        val views: String? = null,
        val editability: Editability? = null,
        val publiceditability: Publiceditability? = null,
        val usage: Usage? = null,
        val comments: Comments? = null,
        val notes: Notes? = null,
        val people: People? = null,
        val tags: Tags? = null,
        val urls: Urls? = null,
        val media: String? = null
    ) {
        @Serializable
        data class Owner(
            val nsid: String? = null,
            val username: String? = null,
            val realname: String? = null,
            val location: String? = null,
            val iconserver: String? = null,
            val iconfarm: Int? = null,
            @SerialName("path_alias") val pathAlias: String? = null,
            val gift: Gift? = null
        ) {
            @Serializable
            data class Gift(
                @SerialName("gift_eligible")
                val giftEligible: Boolean? = null,
                @SerialName("eligible_durations")
                val eligibleDurations: List<String>? = null,
                @SerialName("new_flow")
                val newFlow: Boolean? = null
            )
        }

        @Serializable
        data class Title(
            @SerialName("_content")
            val content: String? = null
        )

        @Serializable
        data class Description(
            @SerialName("_content")
            val content: String? = null
        )

        @Serializable
        data class Visibility(
            val ispublic: Int? = null,
            val isfriend: Int? = null,
            val isfamily: Int? = null
        )

        @Serializable
        data class Dates(
            val posted: String? = null,
            val taken: String? = null,
            val takengranularity: Int? = null,
            val takenunknown: String? = null,
            val lastupdate: String? = null
        )

        @Serializable
        data class Editability(
            val cancomment: Int? = null,
            val canaddmeta: Int? = null
        )

        @Serializable
        data class Publiceditability(
            val cancomment: Int? = null,
            val canaddmeta: Int? = null
        )

        @Serializable
        data class Usage(
            val candownload: Int? = null,
            val canblog: Int? = null,
            val canprint: Int? = null,
            val canshare: Int? = null
        )

        @Serializable
        data class Comments(
            @SerialName("_content")
            val content: String? = null
        )

        @Serializable
        data class Notes(
            val note: List<Note>? = null
        ) {
            @Serializable
            data class Note(
                val id: String? = null,
                val author: String? = null,
                val authorname: String? = null,
                val x: Int? = null,
                val y: Int? = null,
                val w: Int? = null,
                val h: Int? = null,
                @SerialName("_content")
                val content: String? = null
            )
        }

        @Serializable
        data class People(
            val haspeople: Int? = null
        )

        @Serializable
        data class Tags(
            val tag: List<Tag>? = null
        ) {
            @Serializable
            data class Tag(
                val id: String? = null,
                val author: String? = null,
                val authorname: String? = null,
                val raw: String? = null,
                @SerialName("_content")
                val content: String? = null
            )
        }

        @Serializable
        data class Urls(
            val url: List<Url>? = null
        ) {
            @Serializable
            data class Url(
                val type: String? = null,
                @SerialName("_content")
                val content: String? = null
            )
        }
    }
}