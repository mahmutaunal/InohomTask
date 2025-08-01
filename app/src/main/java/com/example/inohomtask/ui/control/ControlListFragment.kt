package com.example.inohomtask.ui.control

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inohomtask.R
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
    private lateinit var adapter: ControlAdapter

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
     * - a menu icon specific to the menu type
     * - a custom back "Geri" text view to navigate back
     */
    private fun setupToolbar() {
        val toolbar = binding.tbControl
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val menu = args.menu

        // Custom back button as text
        val backTextView = TextView(requireContext()).apply {
            text = getString(R.string.back)
            setTextColor(Color.WHITE)
            textSize = 16f
            setPadding(24, 0, 24, 0)

            setOnClickListener {
                findNavController().navigateUp()
            }
        }

        // Set toolbar title and icon dynamically
        toolbar.title = getString(menu.type.nameRes)
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.menu_control_list)
        toolbar.addView(backTextView)

        val item = toolbar.menu.findItem(R.id.action_control_list)
        item?.icon = ContextCompat.getDrawable(requireContext(), menu.type.iconRes)
    }

    /**
     * Initializes the control adapter and handles item click events.
     */
    private fun setupAdapter() {
        adapter = ControlAdapter()
        binding.rvDevices.adapter = adapter

        adapter.onItemClick = { device ->
            viewModel.toggleDeviceState(device)
        }
    }

    /**
     * Observes the LiveData from ViewModel and updates the device list in the adapter.
     */
    private fun observeViewModel() {
        viewModel.deviceList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}