package com.example.inohomtask.ui.control

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inohomtask.data.model.Device
import com.example.inohomtask.databinding.ItemDeviceBinding

/**
 * Adapter to display each smart device in the control list.
 */
class ControlListAdapter : ListAdapter<Device, ControlListAdapter.DeviceViewHolder>(DeviceDiffCallback()) {

    // External click listener
    var onItemClick: ((Device) -> Unit)? = null

    var updatingIds: Set<String> = emptySet()

    inner class DeviceViewHolder(private val binding: ItemDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(device: Device) {
            val isUpdating = updatingIds.contains(device.id)

            binding.ivDeviceIcon.visibility = if (isUpdating) View.INVISIBLE else View.VISIBLE
            binding.cpiDevice.visibility = if (isUpdating) View.VISIBLE else View.GONE

            binding.tvDeviceName.text = device.name

            binding.root.setOnClickListener {
                if (!isUpdating) {
                    onItemClick?.invoke(device)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * Update the device list along with updating IDs.
     * This ensures loading indicators are shown inline without re-creating the entire list.
     */
    fun submitList(devices: List<Device>, updatingIds: Set<String>) {
        this.updatingIds = updatingIds
        submitList(devices)
    }
}

/**
 * DiffUtil callback for efficient item comparison.
 */
class DeviceDiffCallback : DiffUtil.ItemCallback<Device>() {
    override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean {
        return oldItem == newItem
    }
}