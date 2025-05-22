package com.ideabs.mynotes.auth.presentation.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterRoute(
    navController: NavController
) {
    val viewModel: RegisterViewModel = koinViewModel()
    val state by viewModel.registrationState.collectAsState()

    RegisterScreen(
        viewModel = viewModel
    )

    when (val s = state) {
        is RegistrationState.Loading -> {
            // Show loading indicator
        }

        is RegistrationState.Error -> {
            // Show error message: s.message
        }

        is RegistrationState.Success -> {
            // Navigate to next screen
        }

        else -> {}
    }
}