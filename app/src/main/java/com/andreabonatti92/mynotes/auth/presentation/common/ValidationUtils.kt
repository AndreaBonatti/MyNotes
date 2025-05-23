package com.andreabonatti92.mynotes.auth.presentation.common

import android.util.Patterns

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

/*
 The password is valid if it satisfy all the following conditions:
- Minimum length: At least 8 characters
- At least one lowercase letter
- At least one uppercase letter
- At least one digit
- At least one special character (e.g. !@#\$%^&*)
- No whitespace
 */
fun isValidPassword(password: String): Boolean {
    val passwordPattern = Regex(
        pattern = """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$"""
    )
    return passwordPattern.matches(password)
}

fun getPasswordError(password: String): String? {
    return when {
        password.length < 8 -> "Password must be at least 8 characters"
        !password.any { it.isLowerCase() } -> "Password must include a lowercase letter"
        !password.any { it.isUpperCase() } -> "Password must include an uppercase letter"
        !password.any { it.isDigit() } -> "Password must include a digit"
        !password.any { "!@#\$%^&*()-_+=<>?/".contains(it) } -> "Password must include a special character"
        password.any { it.isWhitespace() } -> "Password cannot contain spaces"
        else -> null
    }
}