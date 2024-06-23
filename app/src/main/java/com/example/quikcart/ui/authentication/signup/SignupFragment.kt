package com.example.quikcart.ui.authentication.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.quikcart.utils.AlertUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: SignupViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
        setupListeners()
    }
    private fun setupListeners() {
        binding.signinText.setOnClickListener {
            navigateToLogin(it)
        }
        binding.SignInButton.setOnClickListener {
            handleSignUp()
        }
    }
    private fun navigateToLogin(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment)
    }
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
    private fun showErrorMessage(message: String) {
        AlertUtil.showSnackbar(requireView(),message)
    }
    private fun createCustomerOnShopify(email: String, username: String, userId: String) {
        val address = Address(
            address1 = " ", city = " ", province = " ", phone = " ",
            zip = " ", last_name = " ", first_name = " ", country = " "
        )
        val customer = Customer(
            firstName = username, lastName = userId, email = email, phone = " ", verifiedEmail = true,
            addresses = listOf(address), password = "000000", password_confirmation = "000000", send_email_welcome = false
        )
        val customerRequest = CustomerRequest(customer)
        viewModel.createCustomer(customerRequest)
        lifecycleScope.launch {
            viewModel.customerCreationState.collect { state ->
                when (state) {
                    is ViewState.Error -> handleCustomerCreationError(state.message)
                    is ViewState.Success -> showErrorMessage("Customer created successfully!")
                    else -> {}
                }
            }
        }
    }

    private fun handleCustomerCreationError(message: String) {
        val errorMessage = try {
            val errorJson = JSONObject(message)
            val customerErrors = errorJson.optJSONObject("errors")?.optJSONObject("customer")
            customerErrors?.toString(2) ?: message
        } catch (e: Exception) {
            message
        }
        showErrorMessage("Error: $errorMessage")
        Log.e("CustomerCreationError", errorMessage)
    }

    private fun handleSignUp() {
        val email = binding.EmailTextField.editText?.text.toString()
        val password = binding.PasswordTextField.editText?.text.toString()
        val confirmPassword = binding.ConfirmPasswordTextField.editText?.text.toString()
        val username = binding.UserNameTextField.editText?.text.toString()

        if (validateInputs(email, password, confirmPassword, username)) {
            performSignUp(User(email, password), email, username)
        } else {
            showErrorMessage("Please fix the errors in the form")
        }
    }


    private fun validateInputs(email: String, password: String, confirmPassword: String, username: String): Boolean {
        var isValid = true

        if (!isValidEmail(email)) {
            binding.EmailTextField.error = "Invalid email address"
            isValid = false
        } else {
            binding.EmailTextField.error = null
        }

        if (!isValidPassword(password)) {
            binding.PasswordTextField.error = "Password must be at least 8 characters"
            isValid = false
        } else {
            binding.PasswordTextField.error = null
        }

        if (confirmPassword != password) {
            binding.ConfirmPasswordTextField.error = "Passwords do not match"
            isValid = false
        } else {
            binding.ConfirmPasswordTextField.error = null
        }

        if (username.isBlank()) {
            binding.UserNameTextField.error = "Username cannot be blank"
            isValid = false
        } else {
            binding.UserNameTextField.error = null
        }

        return isValid
    }

    private fun performSignUp(user: User, email: String, username: String) {
        Log.i("TAG", "performSignUp: $user , $email , $username")
        lifecycleScope.launch {
            viewModel.signUp(user)
            viewModel.authState.collect { state ->
                when (state) {
                    ViewState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is ViewState.Success -> handleSignUpSuccess(state.data, email, username)
                    is ViewState.Error -> handleSignUpError()
                }
            }
        }
    }


    private fun handleSignUpSuccess(userId: String?, email: String, username: String) {
        binding.progressBar.visibility = View.GONE
        if (userId != null) {
            showErrorMessage("Signup successful! Please check your email for verification.")
            createCustomerOnShopify(email, username, userId)
            navigateToLogin(binding.SignInButton)
        } else {
            showErrorMessage("Signup failed. Please try again.")
        }
    }

    private fun handleSignUpError() {
        binding.progressBar.visibility = View.GONE
        showErrorMessage("Signup failed. Please try again.")
    }
}
