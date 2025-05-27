package com.andreabonatti92.mynotes.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val data: TokenData
)