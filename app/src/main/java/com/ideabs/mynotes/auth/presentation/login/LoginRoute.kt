package com.ideabs.mynotes.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoute(
    navController: NavController
) {
    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.loginState.collectAsState()

    LoginScreen(
        viewModel = viewModel,
        onNavigateToRegister = {
            viewModel.resetState()
            navController.navigate("register")
        }
    )

//    LaunchedEffect(state) {
//        if (state is LoginState.Success) {
//            delay(1000L) // wait 1 second
//            viewModel.resetState() // to avoid triggering again
//            navController.navigate("") { // TODO add the real route
//                popUpTo("register") {
//                    inclusive = true
//                }
//            }
//        }
//    }
}