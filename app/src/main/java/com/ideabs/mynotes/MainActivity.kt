package com.ideabs.mynotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ideabs.mynotes.navigation.MyNotesNavHost
import com.ideabs.mynotes.ui.theme.MyNotesTheme
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