package com.andreabonatti92.mynotes.auth.presentation.register

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.andreabonatti92.mynotes.auth.presentation.common.PasswordTextField
import com.andreabonatti92.mynotes.auth.presentation.common.getPasswordError
import com.andreabonatti92.mynotes.auth.presentation.common.isValidEmail
import com.andreabonatti92.mynotes.auth.presentation.common.isValidPassword

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

    val registrationState by viewModel.registrationState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Register", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                val trimmedEmail = it.trim()
                emailError = if (!isValidEmail(trimmedEmail)) "Invalid email" else null
                // Reset any previous registration error when user starts typing
                if (registrationState is RegistrationState.Error) {
                    viewModel.resetState()
                }
            },
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
            onPasswordChange = {
                password = it
                passwordError = getPasswordError(it)
                // Reset any previous registration error when user starts typing
                if (registrationState is RegistrationState.Error) {
                    viewModel.resetState()
                }
            },
            errorMessage = passwordError
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Password must be at least 8 characters, with 1 lowercase, 1 uppercase, 1 digit, and 1 special character.",
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(4.dp))

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

        val isFormValid =
            emailError == null && passwordError == null && email.isNotBlank() && password.isNotBlank()

        Button(
            onClick = {
                keyboardController?.hide()

                viewModel.resetState()

                val trimmedEmail = email.trim()
                val trimmedPassword = password.trim()

                emailError = if (!isValidEmail(trimmedEmail)) "Invalid email" else null
                passwordError = if (!isValidPassword(trimmedPassword))
                    getPasswordError(trimmedPassword) else null

                if (emailError == null && passwordError == null) {
                    viewModel.register(trimmedEmail, trimmedPassword)
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            when (registrationState) {
                is RegistrationState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }

                is RegistrationState.Success -> {
                    Text("Registration successful!", color = Color.Green)
                }

                is RegistrationState.Error -> {
                    Text((registrationState as RegistrationState.Error).message, color = Color.Red)
                }

                else -> {
                    // Optionally keep empty space here
                }
            }
        }
    }
}
