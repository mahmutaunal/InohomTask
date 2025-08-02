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

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _updatingDeviceIds = MutableLiveData<Set<String>>(emptySet())
    val updatingDeviceIds: LiveData<Set<String>> get() = _updatingDeviceIds

    private val gson = Gson()

    init {
        WebSocketManager.listener = this
        sendGetControlListRequest()
    }

    /**
     * Sends the GetControlList WebSocket request.
     */
    private fun sendGetControlListRequest() {
        _isLoading.postValue(true)
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

        val currentSet = _updatingDeviceIds.value ?: emptySet()
        _updatingDeviceIds.postValue(currentSet + device.id)

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

        when (method) {
            "GetControlList" -> {
                val devices = mutableListOf<Device>()
                val paramsArray = jsonObject.getAsJsonArray("params")
                val dataArray = paramsArray[0].asJsonObject["data"].asJsonArray

                for (item in dataArray) {
                    val device = item.asJsonObject
                    devices.add(
                        Device(
                            id = device["id"].asString,
                            name = device["name"].asString,
                            type_id = device["type_id"].asString,
                            current_value = device["current_value"].asInt
                        )
                    )
                }

                _deviceList.postValue(devices)
                _isLoading.postValue(false)
            }

            "OnEntityUpdated" -> {
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

                _updatingDeviceIds.postValue(
                    _updatingDeviceIds.value?.minus(updatedDevices.map { it.id }.toSet())
                        ?: emptySet()
                )

                val updatedMap = updatedDevices.associateBy { it.id }
                val currentList = _deviceList.value?.toMutableList() ?: return
                val newList = currentList.map { updatedMap[it.id] ?: it }

                _deviceList.postValue(newList)
            }
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