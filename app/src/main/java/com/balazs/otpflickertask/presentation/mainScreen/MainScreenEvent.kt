package com.balazs.otpflickertask.presentation.mainScreen

sealed interface MainScreenEvent {
    data object Error : MainScreenEvent
}