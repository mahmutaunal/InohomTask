package com.example.inohomtask.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.inohomtask.R
import com.example.inohomtask.databinding.FragmentLoginBinding
import com.example.inohomtask.viewmodel.LoginViewModel

/**
 * Fragment for handling user login interaction.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        displayAppVersion()
        setupListeners()
        observeViewModel()
    }

    /**
     * Displays the current app version in the version TextView.
     */
    private fun displayAppVersion() {
        val versionName = requireContext().packageManager
            .getPackageInfo(requireContext().packageName, 0).versionName
        binding.textVersion.text = getString(R.string.app_version_format, versionName)
    }

    /**
     * Sets up UI event listeners.
     */
    private fun setupListeners() {
        binding.buttonAccounts.setOnClickListener {
            viewModel.sendLogin("demo", "123456")
        }
    }

    /**
     * Observes LiveData from the ViewModel.
     */
    private fun observeViewModel() {
        viewModel.loginSuccess.observe(viewLifecycleOwner) { success ->
            val messageRes = if (success) {
                R.string.toast_login_success
            } else {
                R.string.toast_login_failed
            }
            Toast.makeText(requireContext(), getString(messageRes), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}