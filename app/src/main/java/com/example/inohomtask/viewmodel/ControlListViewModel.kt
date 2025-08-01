package com.example.inohomtask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inohomtask.data.model.Device
import com.example.inohomtask.data.network.WebSocketManager
import com.google.gson.Gson
import com.google.gson.JsonObject

/**
 * ViewModel responsible for requesting and holding the control list.
 */
class ControlListViewModel : ViewModel(), WebSocketManager.WebSocketListenerInterface {

    private val _deviceList = MutableLiveData<List<Device>>()
    val deviceList: LiveData<List<Device>> get() = _deviceList

    private val gson = Gson()

    init {
        WebSocketManager.listener = this
        sendGetControlListRequest()
    }

    /**
     * Sends the GetControlList WebSocket request.
     */
    private fun sendGetControlListRequest() {
        val request = mapOf(
            "is_request" to true,
            "id" to 5,
            "params" to listOf(mapOf<String, Any>()),
            "method" to "GetControlList"
        )
        WebSocketManager.send(gson.toJson(request))
    }

    /**
     * Sends a command to update a device's control value.
     */
    fun toggleDeviceState(device: Device) {
        val newValue = if (device.current_value == 1) 0 else 1

        val request = mapOf(
            "is_request" to true,
            "id" to 84,
            "params" to listOf(
                mapOf(
                    "id" to device.id,
                    "value" to newValue
                )
            ),
            "method" to "UpdateControlValue"
        )
        WebSocketManager.send(gson.toJson(request))
    }

    override fun onMessageReceived(message: String) {
        val jsonObject = gson.fromJson(message, JsonObject::class.java)
        val method = jsonObject.get("method")?.asString

        if (method == "OnEntityUpdated") {
            val updatedDevices = mutableListOf<Device>()
            val paramsArray = jsonObject.getAsJsonArray("params")

            for (param in paramsArray) {
                val entity = param.asJsonObject["entity"].asJsonObject
                updatedDevices.add(
                    Device(
                        id = entity["id"].asString,
                        name = entity["name"].asString,
                        type_id = entity["type_id"].asString,
                        current_value = entity["current_value"].asInt
                    )
                )
            }

            val updatedMap = updatedDevices.associateBy { it.id }
            val currentList = _deviceList.value?.toMutableList() ?: return
            val newList = currentList.map { updatedMap[it.id] ?: it }
            _deviceList.postValue(newList)
        }
    }

    override fun onConnected() {}
    override fun onDisconnected() {}
    override fun onFailure(t: Throwable) {}

    override fun onCleared() {
        WebSocketManager.disconnect()
        super.onCleared()
    }
}