package com.example.inohomtask.ui.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inohomtask.databinding.FragmentControlListBinding
import com.example.inohomtask.viewmodel.ControlListViewModel

/**
 * Fragment to display smart home device controls.
 */
class ControlListFragment : Fragment() {

    private var _binding: FragmentControlListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ControlListViewModel by viewModels()
    private val args: ControlListFragmentArgs by navArgs()
    private lateinit var adapter: ControlListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupAdapter()
        observeViewModel()
    }

    /**
     * Configures the toolbar with:
     * - a dynamic title based on the selected menu type
     */
    private fun setupToolbar() {
        val toolbar = binding.tbControl
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val menu = args.menu

        // Set toolbar title and icon dynamically
        toolbar.title = getString(menu.type.nameRes)

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Initializes the control adapter and handles item click events.
     */
    private fun setupAdapter() {
        adapter = ControlListAdapter()
        binding.rvDevices.adapter = adapter

        adapter.onItemClick = { device ->
            viewModel.toggleDeviceState(device)
        }
    }

    /**
     * Observes the LiveData from ViewModel and updates the device list in the adapter.
     */
    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvDevices.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.deviceList.observe(viewLifecycleOwner) { list ->
            val updating = viewModel.updatingDeviceIds.value ?: emptySet()
            adapter.submitList(list, updating)
        }

        viewModel.updatingDeviceIds.observe(viewLifecycleOwner) { updatingIds ->
            val list = viewModel.deviceList.value ?: emptyList()
            adapter.submitList(list, updatingIds)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}