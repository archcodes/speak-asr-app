package com.example.speaktakehome

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.speaktakehome.ui.theme.SpeakTakeHomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeakTakeHomeTheme {
                val context = LocalContext.current
                val courses by remember {
                    mutableStateOf(
                        try {
                            val jsonResponse = loadJson(context, "course.json")
                            listOf(parseCoursesJson(jsonResponse))
                        } catch (e: Exception) {
                            Log.e("ARCHANA exception", e.toString())
                            emptyList()
                        }
                    )
                }
                // creating navigation controller map
                val navController = rememberNavController()
                NavHost(
                    navController = navController, startDestination = "courseScreen"
                ) {
                    composable("courseScreen") {
                        Scaffold { innerPadding ->
                            CourseScreen(courses, innerPadding, navController)
                        }
                    }
                    composable("recordScreen") {
                        RecordScreen(context, navController)
                    }
                }
            }
        }
    }
}
