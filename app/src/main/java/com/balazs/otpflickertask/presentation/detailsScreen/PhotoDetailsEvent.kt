package com.balazs.otpflickertask.presentation.detailsScreen

sealed interface PhotoDetailsEvent {
    data object Error : PhotoDetailsEvent
}