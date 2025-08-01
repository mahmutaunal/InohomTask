package com.example.inohomtask.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    private fun setLoading(isLoading: Boolean) {
        binding.buttonAccounts.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /**
     * Observes LiveData from the ViewModel.
     */
    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMenuFragment())
            } else {
                Toast.makeText(requireContext(), requireContext().resources.getString(R.string.toast_login_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}