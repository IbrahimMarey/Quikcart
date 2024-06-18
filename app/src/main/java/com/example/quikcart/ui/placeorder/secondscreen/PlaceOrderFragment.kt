package com.example.quikcart.ui.placeorder.secondscreen

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.quikcart.databinding.FragmentPlaceOrderBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.Customer
import com.example.quikcart.models.entities.Order
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.entities.ShippingAddress
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.utils.AlertUtil
import com.example.quikcart.utils.DateUtil
import com.example.quikcart.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaceOrderFragment : Fragment() {

    private lateinit var totalPrice: String
    private lateinit var viewModel: PlaceOrderViewModel
    private lateinit var binding: FragmentPlaceOrderBinding
    private lateinit var address: AddressResponse
    private lateinit var draftOrder: DraftOrder
    @Inject lateinit var preferencesUtils: PreferencesUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getPassedArgs()
        Log.e("TAG", "onViewCreated: ${DateUtil.getCurrentDateAndTime()}", )
        initializeViewModelVariables()
        binding.vm = viewModel
        observeOnLiveData()
        observeOnStateFlow()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeViewModelVariables() {
        viewModel.totalPrice = totalPrice
        viewModel.shippingFees = "0"
        viewModel.orderResponse = Order(getOrderItem())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getOrderItem():OrdersItem {
        return OrdersItem(
            lineItems = draftOrder.lineItems,
            customer = getCustomerData(), totalPrice = totalPrice,
            totalTax = "0",
            currency = preferencesUtils.getCurrencyType(),
            createdAt = DateUtil.getCurrentDateAndTime(),
            paymentGatewayNames = mutableListOf("Cash"),
            shippingAddress = getShippingAddress()

        )
    }

    private fun getCustomerData():Customer {
        return Customer(
            email = "sama@gmail.com",
            id = preferencesUtils.getCustomerId(),
        )
    }

    private fun getShippingAddress():ShippingAddress {
        return ShippingAddress(
            country = address.country,
            address1 = address.address1,
            address2 = address.address2,
            city = address.city
        )
    }

    private fun observeOnLiveData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                AlertUtil.showProgressDialog(requireContext())
            } else {
                AlertUtil.hideProgressDialog()
            }
        }
    }


    private fun observeOnStateFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is ViewState.Error -> {
                            AlertUtil.showToast(requireContext(), it.message)
                        }

                        is ViewState.Success -> {
                            viewModel.deleteCartItemsById(draftOrder.id.toString())
                            AlertUtil.showToast(requireContext(), "Order is placed successfully")
                        }
                        is ViewState.Loading -> {}

                    }
                }
            }
        }
    }


  /*  private fun sendEmail(message:String) {
        try {

            val senderEmail = "noreply@quikcart-20eb5.firebaseapp.com"
            val password = ""


            val receiverEmail = "naremanashraf389@gmail.com"

            val stringHost = "smtp.gmail.com"

            val properties = System.getProperties()
            properties["mail.smtp.host"] = stringHost
            properties["mail.smtp.port"] = "465"
            properties["mail.smtp.ssl.enable"] = "true"
            properties["mail.smtp.auth"] = "true"

            // Creating a session with authentication
            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, password)
                }
            })

            // Creating a MimeMessage
            val mimeMessage = MimeMessage(session)

            // Adding the recipient's email address
            mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(receiverEmail))

            // Seting the subject and message content
            // You can Specify yours
            mimeMessage.subject = "TEST#01"
            mimeMessage.setText(message)

            // Creating a separate thread to send the email
            val t = Thread {
                try {
                    Transport.send(mimeMessage)
                } catch (e: MessagingException) {
                    // Handling messaging exception
                    Toast.makeText(requireContext(),"Error Occured",Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            t.start()
        } catch (e: AddressException) {
            // Handling address exception
            Toast.makeText(requireContext(),"Error Occured $e",Toast.LENGTH_SHORT).show()
        } catch (e: MessagingException) {
            // Handling messaging exception (e.g. network error)
            Toast.makeText(requireContext(),"Error Occured $e",Toast.LENGTH_SHORT).show()
        }

        // Displaying a toast message indicating that the email was sent successfully
        Toast.makeText(requireContext(),"Sent Succesfully ",Toast.LENGTH_SHORT).show()
    }*/

    private fun getPassedArgs() {
        draftOrder = PlaceOrderFragmentArgs.fromBundle(requireArguments()).draftOrder
        totalPrice = PlaceOrderFragmentArgs.fromBundle(requireArguments()).totalPrice
        address = PlaceOrderFragmentArgs.fromBundle(requireArguments()).address
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[PlaceOrderViewModel::class]
    }

}