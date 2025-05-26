package com.andreabonatti92.mynotes.notes.data.mappers

import androidx.compose.ui.graphics.Color
import com.andreabonatti92.mynotes.notes.data.dto.NoteDto
import com.andreabonatti92.mynotes.notes.domain.Note
import java.time.LocalDateTime

fun NoteDto.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        color = Color(color),
        createdAt = LocalDateTime.parse(createdAt)
    )
}