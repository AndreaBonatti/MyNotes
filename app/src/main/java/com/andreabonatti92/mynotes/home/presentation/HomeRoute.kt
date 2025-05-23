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
        onNavigateToNotes = {}
    )
}