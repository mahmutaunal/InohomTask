package com.example.inohomtask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inohomtask.data.model.Menu
import com.example.inohomtask.data.model.MenuType

/**
 * ViewModel responsible for requesting and holding the control list.
 */
class MenuViewModel : ViewModel() {

    private val _menuList = MutableLiveData<List<Menu>>()
    val menuList: LiveData<List<Menu>> get() = _menuList

    init {
        loadStaticDeviceList()
    }

    /**
     * Loads the static list of devices shown on the control screen.
     */
    private fun loadStaticDeviceList() {
        _menuList.value = MenuType.entries.toTypedArray().mapIndexed { index, type ->
            Menu(id = (index + 1).toString(), type = type)
        }
    }
}