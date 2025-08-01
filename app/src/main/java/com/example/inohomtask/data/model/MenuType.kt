package com.example.inohomtask.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.inohomtask.R
import java.io.Serializable

/**
 * Enum representing static device types with icon and name.
 */
enum class MenuType(
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int
) : Serializable {
    FAVORITES(R.string.device_favorites, R.drawable.ic_star),
    LIGHTING(R.string.device_lighting, R.drawable.ic_lightbulb_off),
    CURTAIN(R.string.device_curtain, R.drawable.ic_blinds),
    SOCKET(R.string.device_socket, R.drawable.ic_power),
    SCENARIO(R.string.device_scenario, R.drawable.ic_movie),
    REMOTE(R.string.device_remote, R.drawable.ic_remote_control),
    CAMERA(R.string.device_camera, R.drawable.ic_camera),
    ALARM(R.string.device_alarm, R.drawable.ic_notifications),
    INTERCOM(R.string.device_intercom, R.drawable.ic_phone),
    SYSTEM(R.string.device_system, R.drawable.ic_tune),
    HEATING(R.string.device_heating, R.drawable.ic_device_thermostat),
    AIR_CONDITIONER(R.string.device_air_conditioner, R.drawable.ic_air),
    SENSOR(R.string.device_sensor, R.drawable.ic_sensors),
    SITE_MANAGEMENT(R.string.device_site_management, R.drawable.ic_groups),
    CONCIERGE(R.string.device_concierge, R.drawable.ic_room_service)
}