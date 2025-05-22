package com.ideabs.mynotes.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoute(
    navController: NavController
) {
    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.loginState.collectAsState()

    LoginScreen(
        viewModel = viewModel,
        onLoginSuccess = { accessToken, refreshToken, tokenType ->
            // TODO
        },
        onNavigateToRegister = {
            navController.navigate("register")
        }
    )

    when (val s = state) {
        is LoginState.Loading -> {
            // Show loading indicator
        }

        is LoginState.Error -> {
            // Show error message: s.message
        }

        is LoginState.Success -> {
            // Navigate to next screen
        }

        else -> {}
    }
}