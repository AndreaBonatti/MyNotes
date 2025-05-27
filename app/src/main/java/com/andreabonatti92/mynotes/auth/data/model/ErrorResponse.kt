package com.andreabonatti92.mynotes.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val detail: String
)