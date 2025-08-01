package com.example.inohomtask.ui.control

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inohomtask.R
import com.example.inohomtask.data.model.Device
import com.example.inohomtask.databinding.ItemDeviceBinding

/**
 * Adapter to display each smart device in the control list.
 */
class ControlAdapter : RecyclerView.Adapter<ControlAdapter.DeviceViewHolder>() {

    private val items = mutableListOf<Device>()

    // External click listener
    var onItemClick: ((Device) -> Unit)? = null

    fun submitList(newList: List<Device>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    inner class DeviceViewHolder(private val binding: ItemDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(device: Device) {
            binding.ivDeviceIcon.setImageResource(
                if (device.current_value == 1) R.drawable.ic_lightbulb_on
                else R.drawable.ic_lightbulb_off
            )

            binding.tvDeviceName.text = device.name

            binding.root.setOnClickListener {
                onItemClick?.invoke(device)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}