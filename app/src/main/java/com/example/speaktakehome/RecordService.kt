package com.example.speaktakehome

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

/**
 * This is where the data class required to parse stream data data is present.
 * Also defining functions to open webSocket connection and stream data.
 */
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
        // sending arrStart to start the streaming session
        webSocket.send(asrStart)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        try {
            val jsonObj = JSONObject(text)
            val type = jsonObj.optString("type")

            when (type) {
                // once asrMetadata event is received, starting to stream
                "asrMetadata" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        startStreaming(context, webSocket)
                    }
                }
                // sending text received to UI thread
                "asrResult" -> {
                    val resultText = jsonObj.optString("text")
                    val isFinal = jsonObj.optBoolean("isFinal", false)
                    Handler(Looper.getMainLooper()).post {
                        onResultReceived(resultText)
                        if (isFinal) {
                            Log.d(this::class.simpleName, "Final message received")
                            webSocket.close(1000, "Asr session complete")
                        }
                    }
                }
                // indicates the connection is closed
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
}

private fun startStreaming(context: Context, webSocket: WebSocket) {
    try {
        val jsonContent = context.assets.open("asr-stream-audio-chunks.json").bufferedReader()
            .use { it.readText() }
        val gson = Gson()
        val listType = object : TypeToken<List<AsrStreamData>>() {}
        val chunks: List<AsrStreamData> = gson.fromJson(jsonContent, listType.type)

        // sending asrStreamPayload
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
        Log.e("StartStreaming", "Exception caught ${e.message}")
    }
}

fun openWebSocket(listener: WebSocketListener): WebSocket {
    val request = Request.Builder()
        .url("wss://speak-api--feature-mobile-websocket-interview.preview.usespeak.dev/v2/ws")
        .addHeader("x-access-token", "DFKKEIO23DSAvsdf")
        .addHeader("x-client-info", "Speak Interview Test").build()
    return OkHttpClient().newWebSocket(request, listener)
}
