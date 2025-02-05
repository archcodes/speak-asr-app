package com.example.speaktakehome

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * This is the ViewModel class for Course screen. Acts as a middle layer between data and the UI components
 */
open class CourseVM(application: Application) : AndroidViewModel(application) {
    private val _course = MutableStateFlow<Course?>(null)
    open val course: StateFlow<Course?> = _course.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val json = loadJson(getApplication(), "course.json")
            if (json.isNotEmpty()) {
                try {
                    val courseData = parseCoursesJson(json)
                    _course.value = courseData
                } catch (e: Exception) {
                    Log.e(this::class.simpleName, "Error parsing course.json")
                }
            } else {
                Log.d(this::class.simpleName, "Empty json")
            }
        }
    }
}