package com.example.quikcart.ui.authentication.signup

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
import com.example.quikcart.databinding.FragmentSignupBinding
import com.example.quikcart.models.entities.User
import com.example.quikcart.ui.authentication.AuthViewModel
import com.example.quikcart.models.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          viewModel = ViewModelProvider (this)[AuthViewModel::class.java]

        binding.SignInButton.setOnClickListener {
            val email = binding.EmailTextField.editText?.text.toString()
            val password = binding.PasswordTextField.editText?.text.toString()
            val username = binding.UserNameTextField.editText?.text.toString()
            if ((isValidEmail(email) && isValidPassword(password)) && username.isNotBlank()) {
                val user = User(email, password)
                lifecycleScope.launch {
                    viewModel.signUp(user)
                    viewModel.authState.collect { state ->
                        when (state) {
                            ViewState.Loading -> {
                                 binding.progressBar.visibility = View.VISIBLE
                            }
                            is ViewState.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val success = state.data
                                if (success) {
                                    Toast.makeText(requireContext(), "Signup successful! Please check your email for verification.", Toast.LENGTH_SHORT).show()
                                    Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment)
                                } else {
                                    Toast.makeText(requireContext(), "Signup failed. Please try again.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else -> {}
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Invalid email or password. Please check your input.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.signinText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}
