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
    private lateinit var email:String
    private lateinit var password:String

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
         email = binding.UserNameTextField.editText?.text.toString()
         password = binding.PasswordTextField.editText?.text.toString()
         login()
        binding.signinText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
    private fun login(){
        binding.SignInButton.setOnClickListener {
            if (isValidEmail(email) && isValidPassword(password)) {
                val user = User(email, password)
                lifecycleScope.launch {
                    viewModel.login(user)
                    viewModel.loginState.collect { state ->
                        when (state) {
                            ViewState.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is ViewState.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val success = state.data
                                if (success) {
                                    Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show()

                                } else {
                                    Toast.makeText(requireContext(), "Sign in failed. Please try again.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            is ViewState.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(), "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Invalid email or password. Please check your input.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}