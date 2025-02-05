package com.example.speaktakehome

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.WebSocket

/**
 * This is the ViewModel class for Record screen. Acts as a middle layer between data and the UI components
 */
open class RecordVM(application: Application) : AndroidViewModel(application) {
    private var webSocket: WebSocket? = null
    private val _text = MutableStateFlow("")
    open val text: StateFlow<String> = _text.asStateFlow()

    fun startWebSocket() {
        val context = getApplication<Application>().applicationContext
        val listener = WebSocketListener(context) { newText ->
            _text.value = newText
        }
        webSocket = openWebSocket(listener)
    }

    override fun onCleared() {
        super.onCleared()
        webSocket?.close(1000, "Asr session complete")
        webSocket = null
    }
}
