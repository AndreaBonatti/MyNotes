package com.andreabonatti92.mynotes.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToNotes: () -> Unit,
    onLogout: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val accessToken by viewModel.accessToken.collectAsState()
    val refreshToken by viewModel.refreshToken.collectAsState()

    val firstLetter = email?.firstOrNull()?.uppercase() ?: "?"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .background(color = Color.Gray, shape = CircleShape)
        ) {
            Text(
                text = firstLetter,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Email: $email")

        Text("Access Token: $accessToken")

        Text("Refresh Token: $refreshToken")

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateToNotes,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to your notes")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.logout(onLogout) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Red)
        ) {
            Text("Logout", color = Color.White)
        }
    }
}