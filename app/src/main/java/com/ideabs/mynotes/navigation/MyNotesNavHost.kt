package com.ideabs.mynotes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ideabs.mynotes.auth.presentation.RegisterRoute

@Composable
fun MyNotesNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "register") {
        composable("register") { RegisterRoute(navController) }
    }
}