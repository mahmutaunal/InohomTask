package com.example.inohomtask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inohomtask.data.network.WebSocketManager
import com.google.gson.Gson

/**
 * ViewModel responsible for handling login-related WebSocket operations.
 */
class LoginViewModel : ViewModel(), WebSocketManager.WebSocketListenerInterface {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val gson = Gson()

    init {
        WebSocketManager.listener = this
        WebSocketManager.connect()
    }

    /**
     * Sends an authentication request via WebSocket.
     */
    fun sendLogin(username: String, password: String) {
        _isLoading.postValue(true)
        val request = mapOf(
            "is_request" to true,
            "id" to 8,
            "params" to listOf(mapOf("username" to username, "password" to password)),
            "method" to "Authenticate"
        )
        val jsonMessage = gson.toJson(request)
        WebSocketManager.send(jsonMessage)
    }

    /**
     * Parses incoming messages to detect successful authentication.
     */
    override fun onMessageReceived(message: String) {
        val jsonObject = gson.fromJson(message, Map::class.java)

        val method = jsonObject["method"] as? String
        val id = (jsonObject["id"] as? Double)?.toInt()
        if (method == "OnAuthenticated" && id == 8) {
            _loginSuccess.postValue(true)
            _isLoading.postValue(false)
        }
    }

    override fun onConnected() {
        // No-op: can be used to update UI
    }

    override fun onDisconnected() {
        // No-op
    }

    override fun onFailure(t: Throwable) {
        _loginSuccess.postValue(false)
        _isLoading.postValue(false)
    }

    override fun onCleared() {
        WebSocketManager.disconnect()
        super.onCleared()
    }
}