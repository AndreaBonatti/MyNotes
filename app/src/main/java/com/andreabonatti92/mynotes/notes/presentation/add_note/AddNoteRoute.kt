package com.andreabonatti92.mynotes.notes.presentation.add_note

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddNoteRoute(
    navController: NavController
) {
    val viewModel: AddNoteViewModel = koinViewModel()

    AddNoteScreen(
        viewModel = viewModel
    )
}
