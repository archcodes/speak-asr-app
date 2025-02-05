package com.example.speaktakehome

import android.content.Context
import android.util.Log
import com.google.gson.Gson

/**
 * This is where all the data classes required to parse Course data is present.
 * Also defining load and parse JSON service functions here.
 */
data class Course(
    val id: String,
    val info: Info,
    val units: List<Units>
)

data class Info(
    val title: String,
    val thumbnailImageUrl: String,
    val subtitle: String
)

data class Units(
    val id: String,
    val days: List<Day>
)

data class Day(
    val id: String,
    val title: String,
    val thumbnailImageUrl: String,
    val subtitle: String,
    val lessons: List<Lesson>?
)

data class Lesson(
    val id: String,
    val title: String,
    val durationMin: Int
)

fun loadJson(context: Context, file: String): String {
    return try {
        val jsonContent = context.assets.open(file).bufferedReader().use { it.readText() }
        jsonContent
    } catch (e: Exception) {
        Log.e("CourseService", "Exception caught loading json : ${e.message}")
        ""
    }
}

fun parseCoursesJson(jsonString: String): Course {
    val gson = Gson()
    return gson.fromJson(jsonString, Course::class.java)
}