package com.andreabonatti92.mynotes.notes.presentation.add_note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddNoteRoute(
    navController: NavController
) {
    val viewModel: AddNoteViewModel = koinViewModel()
    val state by viewModel.addNoteState.collectAsState()

    AddNoteScreen(
        viewModel = viewModel
    )

    LaunchedEffect(state) {
        if (state is AddNoteState.Success) {
            delay(1000L) // wait 1 second
            viewModel.resetState() // to avoid triggering again
            navController.navigate("note_list") {
                popUpTo("add_note") {
                    inclusive = true
                }
            }
        }
    }
}
