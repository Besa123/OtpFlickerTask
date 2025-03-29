package com.balazs.otpflickertask.presentation.mainScreen

sealed interface MainScreenAction {
    data class OnSearchQueryChanged(
        val query: String,
    ) : MainScreenAction
}