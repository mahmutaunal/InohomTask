package com.example.inohomtask.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a menu item
 */
@Parcelize
data class Menu(
    val id: String,
    val type: MenuType
) : Parcelable