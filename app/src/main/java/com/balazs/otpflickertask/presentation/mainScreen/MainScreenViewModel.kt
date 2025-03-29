package com.balazs.otpflickertask.presentation.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.balazs.otpflickertask.domain.model.Pictures.Picture
import com.balazs.otpflickertask.domain.usecase.GetPagingPicturesUseCase
import com.balazs.otpflickertask.domain.usecase.LoadPreviouslySavedSearchTermUseCase
import com.balazs.otpflickertask.domain.usecase.SaveSearchTermUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getPagingPicturesUseCase: GetPagingPicturesUseCase,
    private val saveSearchTermUseCase: SaveSearchTermUseCase,
    private val loadPreviouslySavedSearchTermUseCase: LoadPreviouslySavedSearchTermUseCase,
) : ViewModel() {
    private var saveSearchQueryJob: Deferred<Result<String?>?>? = null
    private var pagingPicturesJob: Deferred<Unit?>? = null

    private val eventChannel = Channel<MainScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(MainScreeState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(FLOW_DELAY),
            initialValue = MainScreeState()
        )

    private val _searchedImages = MutableStateFlow<PagingData<Picture>>(PagingData.empty())
    val searchedImages = _searchedImages.asStateFlow()

    init {
        initSearchTerm()
        listenForSearchTermChanges()
    }

    fun onAction(mainScreenAction: MainScreenAction) {
        when (mainScreenAction) {
            is MainScreenAction.OnSearchQueryChanged -> {
                _state.update { mainScreeState ->
                    mainScreeState.copy(
                        searchTerm = mainScreenAction.query
                    )
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun listenForSearchTermChanges() {
        viewModelScope.launch {
            _state.map { it.searchTerm }
                .debounce(SEARCH_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collectLatest { searchTerm ->
                    saveSearchQueryJob?.cancel()
                    pagingPicturesJob?.cancel()

                    saveSearchQueryJob = async(Dispatchers.IO) {
                        searchTerm?.let {
                            saveSearchTermUseCase(
                                searchTerm = it
                            )
                        }
                    }

                    pagingPicturesJob = async(Dispatchers.IO) {
                        searchTerm?.let {
                            getPagingPicturesUseCase(searchTerm = it)
                                .cachedIn(this)
                                .collect { pagingData ->
                                    _searchedImages.value = pagingData
                                }
                        }
                    }

                    saveSearchQueryJob?.await()
                    pagingPicturesJob?.await()
                }
        }
    }

    private fun initSearchTerm() {
        viewModelScope.launch {
            loadPreviouslySavedSearchTermUseCase()
                .firstOrNull()
                ?.fold(
                    onSuccess = { result ->
                        result?.let {
                            _state.update { mainScreeState ->
                                mainScreeState.copy(
                                    searchTerm = it
                                )
                            }
                        } ?: run {
                            _state.update { mainScreeState ->
                                mainScreeState.copy(
                                    searchTerm = DEFAULT_SEARCH_TERM
                                )
                            }
                        }


                    },
                    onFailure = {
                        eventChannel.send(MainScreenEvent.Error)
                    }
                )
        }
    }

    private companion object {
        const val FLOW_DELAY = 4_000L
        const val DEFAULT_SEARCH_TERM = "dog"
        const val SEARCH_DEBOUNCE_TIME = 500L
    }
}