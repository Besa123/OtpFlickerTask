package com.balazs.otpflickertask.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.balazs.otpflickertask.LocalNavHostContoller
import com.balazs.otpflickertask.presentation.detailsScreen.PhotoDetailsScreenRoot
import com.balazs.otpflickertask.presentation.mainScreen.MainScreenRoot

fun NavGraphBuilder.mainNavigationGraph() {
    navigation<MainGraph>(
        startDestination = MainScreen
    ) {
        composable<MainScreen> {
            val navHostController = LocalNavHostContoller.current
            MainScreenRoot(
                onPictureClick = { chosenPhotoDetails ->
                    navHostController.navigate(
                        PictureDetailScreen(
                            secret = chosenPhotoDetails.secret,
                            id = chosenPhotoDetails.photoId,
                            url = chosenPhotoDetails.photoUrl,
                        )
                    )
                }
            )
        }

        composable<PictureDetailScreen> {
            PhotoDetailsScreenRoot()
        }
    }
}