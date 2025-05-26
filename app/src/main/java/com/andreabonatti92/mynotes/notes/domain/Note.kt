package com.andreabonatti92.mynotes.notes.domain

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val color: Color,
    val createdAt: LocalDateTime
)
