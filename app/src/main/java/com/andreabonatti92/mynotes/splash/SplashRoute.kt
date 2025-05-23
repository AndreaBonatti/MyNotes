package com.andreabonatti92.mynotes.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashRoute(
    navController: NavController
) {
    val viewModel: SplashViewModel = koinViewModel()
    val destination by viewModel.destination.collectAsState()

    SplashScreen(
        destination = destination,
        onNavigate = { route ->
            navController.navigate(route) {
                popUpTo("splash") {
                    inclusive = true
                }
            }
        }
    )
}