package com.example.speaktakehome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.speaktakehome.ui.theme.SpeakTakeHomeTheme

/**
 * This class is the entry point. It is responsible for defining navigation controller map
 * and calling CourseScreen and RecordScreen
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeakTakeHomeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController, startDestination = "courseScreen"
                ) {
                    composable("courseScreen") {
                        val courseVM: CourseVM = viewModel()
                        Scaffold { innerPadding ->
                            CourseScreen(courseVM, innerPadding, navController)
                        }
                    }
                    composable("recordScreen") {
                        val recordVM: RecordVM = viewModel()
                        RecordScreen(recordVM, navController)
                    }
                }
            }
        }
    }
}
