package com.example.quikcart.ui.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.quikcart.databinding.FragmentPaymentBinding
import com.example.quikcart.utils.PreferencesUtils
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit

class PaymentFragment : Fragment() {
    lateinit var binding : FragmentPaymentBinding
    private lateinit var amount:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater,container,false)
        binding.paymentButtonContainer.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    OrderRequest(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = amount)
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                PreferencesUtils.isPayWithPayPal = true
                Navigation.findNavController(requireView()).navigateUp()
                Log.i("TAG", "OrderId: ${approval.data.orderId}")
                Toast.makeText(requireActivity(), "Payment Approved", Toast.LENGTH_SHORT).show()
            },
            onCancel = OnCancel{
                Log.i("TAG", "onViewCreated: ==================== payment canceld")
                Toast.makeText(requireActivity(), "Payment Cancel", Toast.LENGTH_SHORT).show()

            },
            onError = OnError{
                Log.i("TAG", "onViewCreated: ${it}")
                Toast.makeText(requireActivity(), "Payment Error", Toast.LENGTH_SHORT).show()

            }
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        amount = PaymentFragmentArgs.fromBundle(requireArguments()).amount

    }

}