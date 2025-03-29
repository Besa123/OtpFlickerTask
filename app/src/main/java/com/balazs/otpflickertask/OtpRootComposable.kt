package com.balazs.otpflickertask

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.balazs.otpflickertask.graph.MainGraph
import com.balazs.otpflickertask.graph.mainNavigationGraph

@Composable
fun OtpRootComposable() {
    val navController = LocalNavHostContoller.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = LocalSnackbarHostState.current
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainGraph,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            mainNavigationGraph()
        }
    }
}