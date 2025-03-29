package com.balazs.otpflickertask.presentation.detailsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.balazs.otpflickertask.domain.usecase.GetPictureInformationUseCase
import com.balazs.otpflickertask.graph.PictureDetailScreen
import com.balazs.otpflickertask.presentation.mainScreen.MainScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val getPictureInformationUseCase: GetPictureInformationUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val navigationInformation = savedStateHandle.toRoute<PictureDetailScreen>()

    private val eventChannel = Channel<PhotoDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(PhotoDetailsState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(FLOW_DELAY),
            initialValue = PhotoDetailsState()
        )

    init {
        loadPictureInformation()
    }

    private fun loadPictureInformation() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getPictureInformationUseCase(
                photoId = navigationInformation.id,
                secret = navigationInformation.secret,
            ).fold(
                onFailure = {
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(PhotoDetailsEvent.Error)
                },
                onSuccess = { result ->
                    result?.let { photoInfo ->
                        _state.update { photoDetailsState ->
                            photoDetailsState.copy(
                                photoInfo = photoInfo,
                                photoUrl = navigationInformation.url,
                                isLoading = false
                            )
                        }
                    } ?: run {
                        eventChannel.send(PhotoDetailsEvent.Error)
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            )
        }
    }

    private companion object {
        const val FLOW_DELAY = 4_000L
    }
}