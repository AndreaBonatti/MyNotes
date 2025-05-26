package com.andreabonatti92.mynotes.notes.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val id: String,
    val title: String,
    val content: String,
    val color: Int,
    val createdAt: String
)
