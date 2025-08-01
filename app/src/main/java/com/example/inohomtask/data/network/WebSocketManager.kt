package com.example.inohomtask.data.network

import android.util.Log
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

/**
 * Singleton class responsible for managing WebSocket connection.
 * Handles connect, disconnect, send, and receive operations.
 */
object WebSocketManager {

    private const val TAG = "WebSocketManager"
    private const val SOCKET_URL = "ws://64.227.77.73:9095"

    private lateinit var client: OkHttpClient
    private var webSocket: WebSocket? = null

    // Callback interface for notifying connection and message events
    var listener: WebSocketListenerInterface? = null

    /**
     * Initiates the WebSocket connection using OkHttp.
     */
    fun connect() {
        if (::client.isInitialized.not()) {
            client = OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()
        }

        val request = Request.Builder()
            .url(SOCKET_URL)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(ws: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket opened")
                listener?.onConnected()
            }

            override fun onMessage(ws: WebSocket, text: String) {
                Log.d(TAG, "Message received: $text")
                listener?.onMessageReceived(text)
            }

            override fun onMessage(ws: WebSocket, bytes: ByteString) {
                Log.d(TAG, "Binary message received: $bytes")
            }

            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closing: $code / $reason")
                listener?.onDisconnected()
                ws.close(code, reason)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket failure: ${t.message}", t)
                listener?.onFailure(t)
            }
        })
    }

    /**
     * Sends a plain text message through the WebSocket.
     * @return true if the message was sent successfully.
     */
    fun send(message: String): Boolean {
        Log.d(TAG, "Sending message: $message")
        return webSocket?.send(message) ?: false
    }

    /**
     * Closes the WebSocket connection gracefully.
     */
    fun disconnect() {
        webSocket?.close(1000, "Normal closure")
        webSocket = null
    }

    /**
     * Interface to observe WebSocket connection and message events.
     */
    interface WebSocketListenerInterface {
        fun onConnected()
        fun onMessageReceived(message: String)
        fun onDisconnected()
        fun onFailure(t: Throwable)
    }
}