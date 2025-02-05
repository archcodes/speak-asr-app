package com.example.speaktakehome

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

data class AsrStreamData(
    val type: String,
    val chunk: String,
    val isFinal: Boolean
)

class WebSocketListener(
    private val context: Context, private val onResultReceived: (String) -> Unit
) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(this::class.simpleName, "WebSocket connection opened")
        val asrStart = """
            {
                "type": "asrStart",
                "learningLocale": "en-US",
                "metadata": {
                    "deviceAudio": {
                        "inputSampleRate": 16000
                    }
                }
            }
        """.trimIndent()
        webSocket.send(asrStart)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        try {
            val jsonObj = JSONObject(text)
            val type = jsonObj.optString("type")

            when (type) {
                "asrMetadata" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        startStreaming(webSocket)
                    }
                }

                "asrResult" -> {
                    val resultText = jsonObj.optString("text")
                    val isFinal = jsonObj.optBoolean("isFinal", false)
                    (context as Activity).runOnUiThread {
                        onResultReceived(resultText)
                        if (isFinal) {
                            Log.d(this::class.simpleName, "Final message received")
                            webSocket.close(1000, "Asr session complete")
                        }
                    }
                }

                "asrClosed" -> {
                    Log.d(this::class.simpleName, "WebSocket connection closed")
                }

                else -> {
                    Log.e(this::class.simpleName, "Wrong asr type received : $type")
                }
            }
        } catch (e: Exception) {
            Log.e(this::class.simpleName, "Exception caught ${e.message}")
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e(this::class.simpleName, "onFailure : ${t.message}")
    }

    private fun startStreaming(webSocket: WebSocket) {
        try {
            val jsonContent = context.assets.open("asr-stream-audio-chunks.json").bufferedReader()
                .use { it.readText() }
            val gson = Gson()
            val listType = object : TypeToken<List<AsrStreamData>>() {}.type
            val chunks: List<AsrStreamData> = gson.fromJson(jsonContent, listType)

            for (chunk in chunks) {
                val asrStreamPayload = """
                    {
                    	"type": "asrStream",
                    	"chunk": "${chunk.chunk}",
                    	"isFinal": ${chunk.isFinal}
                    }
                """.trimIndent()
                webSocket.send(asrStreamPayload)
            }
        } catch (e: Exception) {
            Log.e(this::class.simpleName, "Exception caught in startStreaming ${e.message}")
        }
    }
}