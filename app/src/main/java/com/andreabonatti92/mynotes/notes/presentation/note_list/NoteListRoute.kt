package com.andreabonatti92.mynotes.notes.presentation.note_list

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteListRoute(
    navController: NavController
) {
    val viewModel: NoteListViewModel = koinViewModel()

    NoteListScreen(
        viewModel = viewModel
    )
}