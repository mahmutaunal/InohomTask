package com.example.inohomtask.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inohomtask.data.model.Menu
import com.example.inohomtask.databinding.ItemMenuBinding

/**
 * Adapter to display each smart device in the control list.
 */
class MenuAdapter : RecyclerView.Adapter<MenuAdapter.DeviceViewHolder>() {

    private val items = mutableListOf<Menu>()

    // External click listener
    var onItemClick: ((Menu) -> Unit)? = null

    fun submitList(newList: List<Menu>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    inner class DeviceViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: Menu) {
            binding.ivMenuIcon.setImageResource(menu.type.iconRes)
            binding.tvMenuName.setText(menu.type.nameRes)

            binding.root.setOnClickListener {
                onItemClick?.invoke(menu)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}