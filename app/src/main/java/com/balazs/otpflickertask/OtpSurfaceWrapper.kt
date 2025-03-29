package com.balazs.otpflickertask

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

val LocalNavHostContoller =
    staticCompositionLocalOf<NavHostController> { error("NavController not provided") }
val LocalSnackbarHostState =
    staticCompositionLocalOf<SnackbarHostState> { error("SnackbarHostState not provided") }

@Composable
fun OtpSurfaceWrapper(content: @Composable () -> Unit) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalNavHostContoller provides navController,
        LocalSnackbarHostState provides snackbarHostState,
    ) {
        content()
    }
}