package com.andreabonatti92.mynotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.andreabonatti92.mynotes.navigation.MyNotesNavHost
import com.andreabonatti92.mynotes.ui.theme.MyNotesTheme
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinContext {
                MyNotesTheme {
                    val navController = rememberNavController()
                    MyNotesNavHost(navController = navController)
                }
            }
        }
    }
}