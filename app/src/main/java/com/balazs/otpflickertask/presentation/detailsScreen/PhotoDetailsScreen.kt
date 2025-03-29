package com.balazs.otpflickertask.presentation.detailsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.balazs.otpflickertask.LocalSnackbarHostState
import com.balazs.otpflickertask.R
import com.balazs.otpflickertask.domain.model.PhotoInfo
import com.balazs.otpflickertask.ui.util.ObserveAsEvents
import kotlinx.coroutines.launch

@Composable
fun PhotoDetailsScreenRoot(
    photoDetailsViewModel: PhotoDetailsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current,
) {
    val context = LocalContext.current
    val state by photoDetailsViewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    ObserveAsEvents(flow = photoDetailsViewModel.events) { event ->
        when (event) {
            PhotoDetailsEvent.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.general_error_message)
                    )
                }
            }
        }
    }

    PhotoDetailsScreen(
        photoInfo = state.photoInfo,
        photoUrl = state.photoUrl,
        isLoading = state.isLoading,
    )
}

@Composable
fun PhotoDetailsScreen(
    photoInfo: PhotoInfo?,
    photoUrl: String?,
    isLoading: Boolean,
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
        ) {
            photoUrl?.let {
                item {
                    var scale by remember { mutableFloatStateOf(1f) }
                    val minScale = 1f
                    val maxScale = 5f

                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.loading_time),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .pointerInput(Unit) {
                                detectTransformGestures { _, _, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(minScale, maxScale)
                                }
                            }
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                    )
                }
            }

            photoInfo?.title?.let {
                item {
                    Text(
                        text = stringResource(R.string.title, it),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            photoInfo?.description?.let {
                item {
                    Text(
                        text = stringResource(R.string.description, it),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            photoInfo?.userName?.let {
                item {
                    Text(
                        text = stringResource(R.string.username, it),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            photoInfo?.realName?.let {
                item {
                    Text(
                        text = stringResource(R.string.real_name, it),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            photoInfo?.photoTaken?.let {
                item {
                    Text(
                        text = stringResource(R.string.photo_taken, it),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            photoInfo?.photoPageUrl?.let {
                item {
                    val uriHandler = LocalUriHandler.current
                    Text(
                        text = "URL: $it",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { uriHandler.openUri(it) }
                    )
                }
            }
        }
    }
}