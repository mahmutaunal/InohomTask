package com.example.inohomtask.data.model

/**
 * Data class representing a device item from GetControlList response.
 */
data class Device(
    val id: String,
    val name: String,
    val type_id: String,
    val current_value: Int
)