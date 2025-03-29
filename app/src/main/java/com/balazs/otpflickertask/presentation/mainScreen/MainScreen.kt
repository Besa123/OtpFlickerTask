@file:OptIn(ExperimentalMaterial3Api::class)

package com.balazs.otpflickertask.presentation.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.balazs.otpflickertask.LocalSnackbarHostState
import com.balazs.otpflickertask.R
import com.balazs.otpflickertask.domain.model.Pictures.Picture
import com.balazs.otpflickertask.graph.navigationData.ChosenPhotoDetails
import com.balazs.otpflickertask.ui.util.ObserveAsEvents
import kotlinx.coroutines.launch

@Composable
fun MainScreenRoot(
    mainScreenViewModel: MainScreenViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current,
    onPictureClick: (ChosenPhotoDetails) -> Unit,
) {
    val context = LocalContext.current
    val state by mainScreenViewModel.state.collectAsStateWithLifecycle()
    val searchedImages = mainScreenViewModel.searchedImages.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()

    ObserveAsEvents(flow = mainScreenViewModel.events) { event ->
        when (event) {
            MainScreenEvent.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.general_error_message)
                    )
                }
            }
        }
    }

    MainScreen(
        searchTerm = state.searchTerm,
        searchedImages = searchedImages,
        onPictureClick = onPictureClick,
        onAction = mainScreenViewModel::onAction,
    )
}

@Composable
private fun MainScreen(
    searchTerm: String?,
    searchedImages: LazyPagingItems<Picture>,
    onAction: (MainScreenAction) -> Unit,
    onPictureClick: (ChosenPhotoDetails) -> Unit
) {
    Box {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchTerm ?: "",
                    onQueryChange = {
                        onAction(
                            MainScreenAction.OnSearchQueryChanged(
                                query = it
                            )
                        )
                    },
                    onSearch = {},
                    expanded = true,
                    onExpandedChange = { },
                    placeholder = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.Gray,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            },
            expanded = true,
            onExpandedChange = { },
        ) {
            val refreshLoadState = searchedImages.loadState.refresh
            if (refreshLoadState is LoadState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 10.dp,
                        vertical = 10.dp
                    )
                ) {
                    items(
                        count = searchedImages.itemCount,
                    ) { index ->
                        searchedImages[index]?.let { picture ->
                            FlickrImage(
                                url = picture.url,
                                onPictureClick = {
                                    onPictureClick(
                                        ChosenPhotoDetails(
                                            photoId = picture.id,
                                            secret = picture.secret,
                                            photoUrl = picture.url
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlickrImage(
    url: String,
    onPictureClick: () -> Unit
) {
    AsyncImage(
        model = url,
        contentDescription = "",
        placeholder = painterResource(id = R.drawable.loading_time),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clickable { onPictureClick() }
            .fillMaxWidth()
            .size(200.dp)
            .clip(RoundedCornerShape(12))
    )
}