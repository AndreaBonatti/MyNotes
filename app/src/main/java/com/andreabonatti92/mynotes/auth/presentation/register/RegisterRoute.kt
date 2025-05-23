package com.andreabonatti92.mynotes.auth.presentation.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterRoute(
    navController: NavController
) {
    val viewModel: RegisterViewModel = koinViewModel()
    val state by viewModel.registrationState.collectAsState()

    RegisterScreen(viewModel = viewModel)

    LaunchedEffect(state) {
        if (state is RegistrationState.Success) {
            delay(1000L) // wait 1 second
            viewModel.resetState() // to avoid triggering again
            navController.navigate("login") {
                popUpTo("register") {
                    inclusive = true
                }
            }
        }
    }
}