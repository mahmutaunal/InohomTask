package com.example.inohomtask.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inohomtask.data.model.Menu
import com.example.inohomtask.databinding.ItemMenuBinding

/**
 * Adapter to display each smart device in the control list.
 */
class MenuAdapter : ListAdapter<Menu, MenuAdapter.MenuViewHolder>(MenuDiffCallback()) {

    // External click listener
    var onItemClick: ((Menu) -> Unit)? = null

    inner class MenuViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: Menu) {
            binding.ivMenuIcon.setImageResource(menu.type.iconRes)
            binding.tvMenuName.setText(menu.type.nameRes)

            binding.root.setOnClickListener {
                onItemClick?.invoke(menu)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

/**
 * DiffUtil for comparing Menu items.
 */
class MenuDiffCallback : DiffUtil.ItemCallback<Menu>() {
    override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
        return oldItem == newItem
    }
}