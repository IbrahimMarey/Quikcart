package com.example.quikcart.ui.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentForgotPasswordBinding
import com.example.quikcart.models.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.resetPasswordButton.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email = binding.emailEditText.text.toString()
        lifecycleScope.launch {
            if (email.isNotEmpty()) {
                viewModel.resetPassword(email)
                lifecycleScope.launch {  }
                viewModel.resetPasswordState.collect { state ->
                    when (state) {
                        is ViewState.Loading -> showLoading(true)
                        is ViewState.Success -> {
                            showLoading(false)
                            Toast.makeText(requireContext(), "Reset email sent successfully", Toast.LENGTH_SHORT).show()
                            navigateToLogin()
                        }
                        is ViewState.Error -> {
                            showLoading(false)
                            Toast.makeText(requireContext(), "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToLogin(){
        Navigation.findNavController(requireView()).navigate(R.id.action_forgotPasswordFragment_to_loginFragment)

    }
}
