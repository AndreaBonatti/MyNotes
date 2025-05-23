package com.andreabonatti92.mynotes.auth.presentation.login

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.andreabonatti92.mynotes.auth.presentation.common.PasswordTextField
import com.andreabonatti92.mynotes.auth.presentation.common.isValidEmail

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                val trimmedEmail = it.trim()
                emailError = if (!isValidEmail(trimmedEmail)) "Invalid Email" else null
                if (loginState is LoginState.Error) {
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
                passwordError = null
                // Reset any previous login error when user starts typing
                if (loginState is LoginState.Error) {
                    viewModel.resetState()
                }
            },
            errorMessage = passwordError
        )

        Spacer(modifier = Modifier.height(16.dp))

        val isFormValid =
            emailError == null && passwordError == null && email.isNotBlank() && password.isNotBlank()

        Button(
            onClick = {
                viewModel.resetState()

                val trimmedEmail = email.trim()
                val trimmedPassword = password.trim()

                emailError = if (!isValidEmail(trimmedEmail)) "Invalid email" else null

                if (emailError == null) {
                    viewModel.login(trimmedEmail, trimmedPassword)
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            when (loginState) {
                is LoginState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }

                is LoginState.Success -> {
                    Text("Login successful!", color = Color.Green)
                }

                is LoginState.Error -> {
                    Text((loginState as LoginState.Error).message, color = Color.Red)
                }

                else -> {
                    // Optionally keep empty space here
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don't have an account?")
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = onNavigateToRegister) {
                Text("Register")
            }
        }
    }
}
