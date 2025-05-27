package com.andreabonatti92.mynotes.notes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val title: String,
    val content: String,
    val color: Int
)
