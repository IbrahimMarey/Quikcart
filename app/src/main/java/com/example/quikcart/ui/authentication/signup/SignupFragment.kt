package com.example.quikcart.ui.authentication.signup

import android.os.Bundle
import android.util.Log
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
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.Address
import com.example.quikcart.models.entities.Customer
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.User
import com.example.quikcart.ui.authentication.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var username: String
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
        email = binding.EmailTextField.editText?.text.toString()
        password = binding.PasswordTextField.editText?.text.toString()
        username = binding.UserNameTextField.editText?.text.toString()
        signUp()
        binding.signinText.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    private fun createCustomerOnShopify(email: String, username: String) {

        val address = Address(
        address1 = "123 Oak St",
        city = "Ottawa",
        province = "ON",
        phone = "555-1212",
        zip = "123 ABC",
        last_name = "Lastnameson",
        first_name = "Mother",
        country = "CA"
        )

        val customer = Customer(
            first_name = "Steve", last_name = "Lastnameson", email = "mayarhassan@gmail.com", phone = "32355512", verified_email = true,
            addresses = listOf(address), password = "newpass", password_confirmation = "newpass", send_email_welcome = false
        )
        val customerRequest = CustomerRequest(customer)
        viewModel.createCustomer(customerRequest)
        lifecycleScope.launch {
            viewModel.customerCreationState.collect { state ->
                when (state) {
                    is ViewState.Error -> {
                        val errorMessage = try {
                            val errorJson = JSONObject(state.message)
                            val customerErrors = errorJson.optJSONObject("errors")?.optJSONObject("customer")
                            customerErrors?.toString(2) ?: state.message
                        } catch (e: Exception) {
                            state.message
                        }
                        Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_LONG).show()
                        Log.e("CustomerCreationError", errorMessage)
                    }

                    else -> {}
                }
            }
        }
    }
    private fun signUp(){
        binding.SignInButton.setOnClickListener {
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
                                    createCustomerOnShopify(email, username)
                                    Navigation.findNavController(it).navigate(R.id.action_signupFragment_to_loginFragment)
                                } else {
                                    Toast.makeText(requireContext(), "Signup failed. Please try again.", Toast.LENGTH_SHORT).show()
                                }
                            }
                           is ViewState.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(), "Signup failed. Please try again.", Toast.LENGTH_SHORT).show()
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
