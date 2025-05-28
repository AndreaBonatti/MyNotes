package com.andreabonatti92.mynotes.notes.presentation.add_note

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@Composable
fun AddNoteScreen(viewModel: AddNoteViewModel) {
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var titleError by rememberSaveable { mutableStateOf<String?>(null) }
    var contentError by rememberSaveable { mutableStateOf<String?>(null) }
    val colorSaver = Saver<Color, Int>(
        save = { it.toArgb() },
        restore = { Color(it) }
    )

    var selectedColor by rememberSaveable(stateSaver = colorSaver) {
        mutableStateOf(Color.Yellow)
    }

    val addNoteState by viewModel.addNoteState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val isFormValid =
        titleError == null && contentError == null && title.isNotBlank() && content.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Add Note", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                titleError = if (it.isBlank()) "Title cannot be empty" else null
                if (addNoteState is AddNoteState.Error) viewModel.resetState()
            },
            label = { Text("Title") },
            isError = titleError != null,
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.height(16.dp)) {
            androidx.compose.animation.AnimatedVisibility(
                visible = titleError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = titleError ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = {
                content = it
                contentError = if (it.isBlank()) "Content cannot be empty" else null
                if (addNoteState is AddNoteState.Error) viewModel.resetState()
            },
            label = { Text("Content") },
            isError = contentError != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 10
        )

        Box(modifier = Modifier.height(16.dp)) {
            androidx.compose.animation.AnimatedVisibility(
                visible = contentError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = contentError.orEmpty(),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Select Note Color:")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(Color.Yellow, Color.Cyan, Color.Magenta, Color.LightGray).forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color)
                        .clickable { selectedColor = color }
                        .border(
                            width = 2.dp,
                            color = if (selectedColor == color) Color.Black else Color.Transparent
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                keyboardController?.hide()

                viewModel.resetState()

                titleError = if (title.isBlank()) "Title cannot be empty" else null
                contentError = if (content.isBlank()) "Content cannot be empty" else null

                if (titleError == null && contentError == null) {
                    viewModel.addNote(title.trim(), content.trim(), selectedColor.toArgb())
                }
            },
            enabled = isFormValid && addNoteState !is AddNoteState.Uploading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Note")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            when (addNoteState) {
                is AddNoteState.Uploading -> {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }

                is AddNoteState.Success -> {
                    Text("Note added successfully!", color = Color.Green)
                }

                is AddNoteState.Error -> {
                    Text((addNoteState as AddNoteState.Error).message, color = Color.Red)
                }

                else -> Unit
            }
        }
    }
}

