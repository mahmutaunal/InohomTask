package com.example.inohomtask.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.inohomtask.R
import com.example.inohomtask.data.model.MenuType
import com.example.inohomtask.databinding.FragmentMenuBinding
import com.example.inohomtask.viewmodel.MenuViewModel

/**
 * Fragment to display smart home device controls after login.
 */
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MenuViewModel by viewModels()
    private lateinit var adapter: MenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbarMenuClickListener()
        setupAdapter()
        observeViewModel()
    }

    /**
     * Sets up the toolbar menu click listener.
     */
    private fun setupToolbarMenuClickListener() {
        binding.tbMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Initializes the menu adapter and handles item click events.
     */
    private fun setupAdapter() {
        adapter = MenuAdapter()
        binding.rvMenu.adapter = adapter

        adapter.onItemClick = { menu ->
            when (menu.type) {
                MenuType.LIGHTING -> {
                    findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToControlListFragment(menu))
                }
                else -> {
                    Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Observes the menu list LiveData from the ViewModel and updates the UI.
     */
    private fun observeViewModel() {
        viewModel.menuList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}