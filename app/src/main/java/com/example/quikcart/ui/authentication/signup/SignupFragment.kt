package com.example.quikcart.ui.authentication.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.quikcart.databinding.FragmentSignupBinding 
import com.example.quikcart.models.entities.User
import com.example.quikcart.ui.authentication.AuthViewModel
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.signUpResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Signup successful! Please check your email for verification.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Signup failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.SignInButton.setOnClickListener {
            val email = binding.EmailTextField.editText?.text.toString()
            val password = binding.PasswordTextField.editText?.text.toString()
            if (email.isNotBlank() && password.isNotBlank()) {
                val user = User(email, password)
                lifecycleScope.launch {
                    viewModel.signUp(user)
                }
            } else {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}