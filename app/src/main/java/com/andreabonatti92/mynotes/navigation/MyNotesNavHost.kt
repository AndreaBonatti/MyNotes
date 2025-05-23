package com.andreabonatti92.mynotes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.andreabonatti92.mynotes.auth.presentation.login.LoginRoute
import com.andreabonatti92.mynotes.auth.presentation.register.RegisterRoute
import com.andreabonatti92.mynotes.home.presentation.HomeRoute
import com.andreabonatti92.mynotes.splash.SplashRoute

@Composable
fun MyNotesNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("register") { RegisterRoute(navController) }
        composable("login") { LoginRoute(navController) }
        composable("home") { HomeRoute(navController) }
        composable("splash") { SplashRoute(navController) }
    }
}