package com.example.speaktakehome

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

/**
 * This is where the UI components for the first screen - Courses - is defined
 */
@Composable
fun CourseScreen(
    courseVM: CourseVM = viewModel(), innerPadding: PaddingValues, navController: NavHostController
) {
    val course by courseVM.course.collectAsState()

    if (course == null) {
        // initial state. Can implement loading sign
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color(0xffF5F5F5))
        ) {
            item {
                CourseItem(course = course!!)
            }
            course!!.units.forEach { unit ->
                item {
                    Text(
                        text = unit.id.replace("unit_", "Unit "),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(unit.days) { day ->
                    DayItem(day) { navController.navigate("recordScreen") }
                }
            }
        }
    }
}

@Composable
fun CourseItem(course: Course) {
    Log.i("ARCHANA", course.info.title)
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Image(
            painterResource(R.drawable.course_default_header_background_vector),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = course.info.thumbnailImageUrl,
                contentDescription = "Course thumbnail",
                modifier = Modifier
                    .size(99.dp)
                    .clip(CircleShape)
                    .border(2.dp, color = Color.White, shape = CircleShape)
                    .background(Color.White)
                    .shadow(
                        elevation = 1.dp, shape = CircleShape, clip = false, spotColor = Color.White
                    ),
                contentScale = ContentScale.Crop
            )
            val totalDays = course.units.sumOf { it.days.size }
            Text(text = course.info.subtitle + " . $totalDays DAYS", color = Color.Gray)
            Text(
                text = course.info.title,
                modifier = Modifier.padding(bottom = 36.dp),
                fontWeight = FontWeight.Bold
            )
            Image(
                painterResource(R.drawable.course_unit_default_icon),
                contentDescription = "Course default image"
            )
        }
    }
}

@Composable
fun DayItem(day: Day, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(start = 12.dp, end = 12.dp)
    ) {
        Text(
            text = day.id.replace(
                "day_", if ((day.id).substring(4).toInt() < 10) "DAY " + 0 else "DAY "
            ),
            color = if (day.id == "day_0") Color.Blue else Color.Gray,
            fontWeight = if (day.id == "day_0") FontWeight.Bold else FontWeight.Normal
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = day.thumbnailImageUrl,
                    contentDescription = "Course thumbnail",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, color = Color.LightGray, shape = CircleShape)
                        .background(
                            Color.White
                        )
                        .shadow(
                            elevation = 1.dp,
                            shape = CircleShape,
                            clip = false,
                            spotColor = Color.White
                        ),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(24.dp))
                Column {
                    Text(text = day.title, fontWeight = FontWeight.Bold)
                    Text(text = day.subtitle, color = Color.Gray)
                }
            }
        }
    }
}