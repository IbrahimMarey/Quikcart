package com.example.quikcart.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentLoginBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.User
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.ui.MainActivity
import com.example.quikcart.utils.AlertUtil
import com.example.quikcart.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    @Inject lateinit var preferencesUtils: PreferencesUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getProducts()
        setupListeners()
        guestMode()
    }

    private fun guestMode() {
        binding.skipText.setOnClickListener {
            PreferencesUtils.getInstance(requireContext()).setUserID("-1")
            PreferencesUtils.getInstance(requireContext()).setCustomerId(-1)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setupListeners() {
        binding.signinText.setOnClickListener {
            navigateToSignUp()
        }
        binding.SignInButton.setOnClickListener {
            handleLogin()
        }
        binding.forgetTextView.setOnClickListener {
            navigateToForgotPassword()
        }
    }

    private fun navigateToSignUp() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_signupFragment)
    }

    private fun navigateToForgotPassword() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    private fun showMessage(message: String) {
        AlertUtil.showSnackbar(requireView(),message)
    }

    private fun handleLogin() {
        val email = binding.UserNameTextField.editText?.text.toString()
        val password = binding.PasswordTextField.editText?.text.toString()

        if (validateInputs(email, password)) {
            performLogin(User(email, password))
            fetchDraftOrdersForCustomer(email)
        } else {
            showMessage("Invalid email or password. Please check your input.")
            showLoading(false)
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
            binding.PasswordTextField.error = " Invalid Password "
            isValid = false
        } else {
            binding.PasswordTextField.error = null
        }

        return isValid
    }

    private fun performLogin(user: User) {
        showLoading(true)
        lifecycleScope.launch {
            viewModel.login(user)
            viewModel.loginState.collect { state ->
                when (state) {
                    ViewState.Loading -> showLoading(true)
                    is ViewState.Success -> {
                        showLoading(false)
                        handleLoginSuccess(state.data)
                    }
                    is ViewState.Error -> {
                        showLoading(false)
                        handleLoginError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleLoginSuccess(userId: String?) {
        showLoading(false)
        try {
            if (userId != null) {
                PreferencesUtils.getInstance(requireContext()).setUserID(userId)
                getCurrentCustomerID(userId)
                preferencesUtils.saveCustomerEmail(binding.UserNameTextField.editText?.text.toString())
                showMessage("Sign in successful")
            } else {
                showMessage("Sign in failed. Please try again.")
            }
        } catch (e: Exception) {
        }
    }

    private fun handleLoginError(message: String) {
        showLoading(false)
        val errorMessage = try {
            val errorJson = JSONObject(message)
            val errors = errorJson.optJSONObject("errors")
            errors?.toString(2) ?: message
        } catch (e: Exception) {
            message
        }
        showMessage("Error: $errorMessage")
    }


    private fun getCurrentCustomerID(id: String) {
        lifecycleScope.launch {
            viewModel.getCustomer()
            viewModel.customerCreationState.collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        val currentCustomer = state.data.filter { it.lastName == id }
                        if (currentCustomer.isNotEmpty()) {
                            PreferencesUtils.getInstance(requireContext()).setCustomerId(currentCustomer[0].id)
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        } else {
                            showMessage("Customer not found")
                        }
                    }
                    is ViewState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showMessage("Error: ${state.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    private fun fetchDraftOrdersForCustomer(email: String) {
        lifecycleScope.launch {
            viewModel.getAllDraftOrder()
            viewModel.allDraftOrder.collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        val draftOrders = state.data.filter { it.email == email }
                        if (draftOrders.isNotEmpty()) {
                            if (draftOrders.size == 2){
                                when (draftOrders[0].lineItems[0].price == "0.00") {
                                    true -> {
                                        PreferencesUtils.getInstance(requireContext()).setFavouriteId(draftOrders[0].id)
                                        PreferencesUtils.getInstance(requireContext()).setCartId(draftOrders[1].id)
                                        fetchProducts(draftOrders[0])
                                    }
                                    false -> {
                                        PreferencesUtils.getInstance(requireContext()).setCartId(draftOrders[0].id)
                                        PreferencesUtils.getInstance(requireContext()).setFavouriteId(draftOrders[1].id)
                                        fetchProducts(draftOrders[1])
                                    }
                                }
                            }else{
                                when (draftOrders[0].lineItems[0].price == "0.00") {
                                    true -> {
                                        PreferencesUtils.getInstance(requireContext()).setFavouriteId(draftOrders[0].id)
                                        fetchProducts(draftOrders[0])
                                    }
                                    false -> {
                                        PreferencesUtils.getInstance(requireContext()).setCartId(draftOrders[0].id)
                                    }
                                }
                            }
                        } else {
                            PreferencesUtils.getInstance(requireContext()).setFavouriteId(0)
                            PreferencesUtils.getInstance(requireContext()).setCartId(0)
                        }
                    }
                    is ViewState.Error -> {
                        showMessage("Error: ${state.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    private fun fetchProducts(draftOrder: DraftOrder) {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ViewState.Success -> {
                            val products = state.data
                            val matchingProducts = mutableListOf<ProductsItem>()
                            draftOrder.lineItems.forEach { lineItem ->
                                val matchingProduct = products.find { it.title == lineItem.title }
                                if (matchingProduct != null) {
                                    matchingProducts.add(matchingProduct)
                                }
                            }

                            for (product in matchingProducts) {
                                viewModel.inertProducts(product)
                            }
                        }
                        is ViewState.Error -> {
                        }
                        is ViewState.Loading -> {
                        }
                    }
                }
            }
        }
    }
}
