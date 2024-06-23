package com.example.quikcart.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentCartBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.models.entities.cart.LineItem
import com.example.quikcart.utils.PreferencesUtils
import com.example.quikcart.utils.setPrice
import com.google.android.material.snackbar.Snackbar
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var cartAdapter: CartAdapter
    private lateinit var pref: PreferencesUtils
    private var cartTotalPrice:Float = 0.0f
    private lateinit var cartDraftOrder: DraftOrder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        setInitialUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = PreferencesUtils.getInstance(requireActivity())
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        viewModel.getCart(pref.getCartId().toString())
        val delAction: (LineItem) -> Unit = {
            delCartItem(it)
        }
        val editItemQuantity: (LineItem, Int,Float) -> Unit = { lineItem, index,newPrice ->
            viewModel.lineItemsList[index] = lineItem
            cartTotalPrice+=newPrice
            cartDraftOrder.totalPrice = cartTotalPrice.toString()
            navigateToConfirmOrderFirstScreen(cartDraftOrder)
            binding.cartTotalPrice.setPrice(cartTotalPrice,requireActivity())
        }
        cartAdapter = CartAdapter(delAction,editItemQuantity)
        gridLayoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerCart.apply {
            adapter = cartAdapter
            layoutManager = gridLayoutManager
        }
        setUpUI()
    }

    override fun onPause() {
        super.onPause()
        if (pref.getCartId().toString() != "0" && pref.getCartId().toString() != "-1")
            viewModel.saveCartWhileLeaving(pref.getCartId().toString())
    }

    private fun setInitialUI() {
        binding.cartProgress.visibility = View.VISIBLE
        binding.cartEmpty.visibility = View.GONE
        binding.recyclerCart.visibility = View.GONE
        binding.cartCardAddToPayment.visibility = View.GONE
    }

    private fun setUpUI() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.cart.collectLatest {
                when (it) {
                    is ViewState.Loading -> {
                        setInitialUI()
                    }

                    is ViewState.Error -> {
                        setUPErrorOrEmptyCart()
                    }

                    is ViewState.Success -> {
                        if (it.data.lineItems.isNotEmpty()) {
                            setUPSuccessCart(it.data)
                            navigateToConfirmOrderFirstScreen(it.data)
                        } else {
                            setUPErrorOrEmptyCart()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToConfirmOrderFirstScreen(draftOrder : DraftOrder) {
        binding.proceedToPayBtn.setOnClickListener {
//            val action =
  //              CartFragmentDirections.actionCartFragmentToConfirmOrderFirstScreenFragment(draftOrder)
            val action =
                CartFragmentDirections.actionCartFragmentToPlaceOrderFragment(draftOrder)

            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun setUPErrorOrEmptyCart() {
        binding.cartProgress.visibility = View.GONE
        binding.cartEmpty.visibility = View.VISIBLE
        binding.recyclerCart.visibility = View.GONE
        binding.cartCardAddToPayment.visibility = View.GONE
    }

    private fun delCartItem(item: LineItem) {
        Snackbar.make(
            requireView(),
            getString(R.string.are_you_sure_you_want_to_delete_this_address),
            Snackbar.LENGTH_LONG
        )
            .setAction(getString(R.string.delete)) {
                if (viewModel.lineItemsList.size >= 2)
                    viewModel.delCartItem(pref.getCartId().toString(), item)
                else {
                    val id = pref.getCartId().toString()
                    viewModel.delCart(id)
                    pref.setCartId(0)
                }
            }.show()
    }

    private fun setUPSuccessCart(cart: DraftOrder) {
        binding.cartProgress.visibility = View.GONE
        binding.cartEmpty.visibility = View.GONE
        binding.recyclerCart.visibility = View.VISIBLE
        binding.cartCardAddToPayment.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.Main) {
            cartAdapter.submitList(viewModel.getProducts(cart.lineItems))
        }
        cartTotalPrice = cart.totalPrice.toFloat()
        binding.cartTotalPrice.setPrice(cartTotalPrice, requireActivity())
        cartDraftOrder = cart
    }
}