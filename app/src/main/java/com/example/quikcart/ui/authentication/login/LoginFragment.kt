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
import com.example.quikcart.databinding.FragmentLoginBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.User
import com.example.quikcart.ui.authentication.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        setupListeners()
    }

    private fun setupListeners() {
        binding.signinText.setOnClickListener {
            navigateToSignUp(it)
        }
        binding.SignInButton.setOnClickListener {
            handleLogin()
        }
    }

    private fun navigateToSignUp(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun handleLogin() {
        val email = binding.UserNameTextField.editText?.text.toString()
        val password = binding.PasswordTextField.editText?.text.toString()

        if (validateInputs(email, password)) {
            performLogin(User(email, password))
        } else {
            showErrorMessage("Invalid email or password. Please check your input.")
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        var isValid = true

        if (!isValidEmail(email)) {
            binding.UserNameTextField.error = "Invalid email address"
            isValid = false
        } else {
            binding.UserNameTextField.error = null
        }

        if (!isValidPassword(password)) {
            binding.PasswordTextField.error = "Password must be at least 8 characters"
            isValid = false
        } else {
            binding.PasswordTextField.error = null
        }

        return isValid
    }

    private fun performLogin(user: User) {
        lifecycleScope.launch {
            viewModel.login(user)
            viewModel.loginState.collect { state ->
                when (state) {
                    ViewState.Loading -> showLoading(true)
                    is ViewState.Success -> handleLoginSuccess(state.data)
                    is ViewState.Error -> handleLoginError(state.message)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleLoginSuccess(isSuccess: Boolean) {
        showLoading(false)
        if (isSuccess) {
            showErrorMessage("Sign in successful")
        } else {
            showErrorMessage("Sign in failed. Please try again.")
        }
    }

    private fun handleLoginError(message: String) {
        showLoading(false)
        showErrorMessage("Error: $message")
    }
}
