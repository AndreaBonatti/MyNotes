package com.andreabonatti92.mynotes.home.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    navController: NavController
) {
    val viewModel: HomeViewModel = koinViewModel()

    HomeScreen(
        viewModel = viewModel,
        onNavigateToNotes = {
            navController.navigate("note_list") {
                // TODO popUpTo is needed?
//                popUpTo("") {
//                    inclusive = true
//                }
            }
        },
        onLogout = {
            navController.navigate("login") {
                popUpTo("home") {
                    inclusive = true
                }
            }
        }
    )
}