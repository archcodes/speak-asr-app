package com.example.speaktakehome

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import okhttp3.OkHttpClient
import okhttp3.Request

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(context: Context, navController: NavController) {
    val recognizedText = remember { mutableStateOf("") }
    val webSocketListener = WebSocketListener(context) { text ->
        recognizedText.value = text
    }
    Scaffold(topBar = {
        TopAppBar(title = {}, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Filled.ArrowBack, contentDescription = "Go back to course screen"
                )
            }
        })
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    recognizedText.value,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(64.dp))
                Image(painterResource(R.drawable.uploadicon),
                    contentDescription = "Upload audio",
                    Modifier
                        .size(64.dp)
                        .clickable { openWebSocket(webSocketListener) })
            }
        }
    }
}

fun openWebSocket(listener: WebSocketListener) {
    val request = Request.Builder()
        .url("wss://speak-api--feature-mobile-websocket-interview.preview.usespeak.dev/v2/ws")
        .addHeader("x-access-token", "DFKKEIO23DSAvsdf")
        .addHeader("x-client-info", "Speak Interview Test").build()
    OkHttpClient().newWebSocket(request, listener)
}