package com.ideabs.mynotes.auth.presentation

import android.util.Patterns
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ideabs.mynotes.R

@Composable
fun RegisterScreen(
    onRegisterClicked: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Register", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = emailError != null,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Box(modifier = Modifier.height(16.dp)) {
            androidx.compose.animation.AnimatedVisibility(
                visible = emailError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = emailError ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            password = password,
            onPasswordChange = { password = it },
            errorMessage = passwordError
        )

        Box(modifier = Modifier.height(16.dp)) {
            androidx.compose.animation.AnimatedVisibility(
                visible = passwordError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = passwordError ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val trimmedEmail = email.trim()
                val trimmedPassword = password.trim()

                emailError = if (!isValidEmail(trimmedEmail)) "Invalid email" else null
                passwordError = if (!isValidPassword(trimmedPassword))
                    getPasswordError(trimmedPassword) else null

                if (emailError == null && passwordError == null) {
                    onRegisterClicked(trimmedEmail, trimmedPassword)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }
    }
}

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        isError = errorMessage != null,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        trailingIcon = {
            val image = if (passwordVisible)
                ImageVector.vectorResource(R.drawable.ic_visibility)
            else
                ImageVector.vectorResource(R.drawable.ic_visibility_off)

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
}

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

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    RegisterScreen { email, password ->
    }
}