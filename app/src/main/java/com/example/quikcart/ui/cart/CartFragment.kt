package com.example.quikcart.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentCartBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.models.entities.cart.LineItem
import com.example.quikcart.utils.PreferencesUtils
import com.example.quikcart.utils.setPrice
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var gridLayoutManager : GridLayoutManager
    private lateinit var cartAdapter: CartAdapter
    lateinit var pref : PreferencesUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater,container,false)
        setInitialUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = PreferencesUtils.getInstance(requireActivity())
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        viewModel.getCart(pref.getCartId().toString())
        binding.proceedToPayBtn.setOnClickListener{
            val action = CartFragmentDirections.actionCartFragmentToPaymentFragment()
            Navigation.findNavController(it).navigate(action)
        }

        val delAction :(LineItem)->Unit= {
            delCartItem(it)
        }
        cartAdapter = CartAdapter(delAction)
        gridLayoutManager = GridLayoutManager(requireActivity(),2)
        binding.recyclerCart.apply {
            adapter = cartAdapter
            layoutManager = gridLayoutManager
        }
        setUpUI()
    }

    private fun setInitialUI()
    {
        binding.cartProgress.visibility = View.VISIBLE
        binding.cartEmpty.visibility = View.GONE
        binding.recyclerCart.visibility = View.GONE
        binding.cartCardAddToPayment.visibility = View.GONE
    }
    private fun setUpUI()
    {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.cart.collectLatest {
                when(it){
                   is ViewState.Loading -> {
                       setInitialUI()
                   }
                    is ViewState.Error -> {
                        setUPErrorOrEmptyCart()
                    }
                    is ViewState.Success -> {
                        if (it.data.lineItems.isNotEmpty())
                        {
                            binding.cartProgress.visibility = View.GONE
                            binding.cartEmpty.visibility = View.GONE
                            binding.recyclerCart.visibility = View.VISIBLE
                            binding.cartCardAddToPayment.visibility = View.VISIBLE
                            if (it.data.lineItems.isNotEmpty())
                                cartAdapter.submitList(it.data.lineItems)
                            binding.cartTotalPrice.setPrice(it.data.totalPrice.toFloat(),requireActivity())
                        }else{
                            setUPErrorOrEmptyCart()
                        }
                    }
                }
            }
        }
    }
    private fun setUPErrorOrEmptyCart(){
        binding.cartProgress.visibility = View.GONE
        binding.cartEmpty.visibility = View.VISIBLE
        binding.recyclerCart.visibility = View.GONE
        binding.cartCardAddToPayment.visibility = View.GONE
    }
    private fun delCartItem(item: LineItem){
        Snackbar.make(requireView(), getString(R.string.are_you_sure_you_want_to_delete_this_address), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.delete)) {
                if (viewModel.lineItemsList.size >=2)
                    viewModel.delCartItem(pref.getCartId().toString(),item)
                else
                {
                    val id = pref.getCartId().toString()
                    viewModel.delCart(id)
                    pref.setCartId(0)
                }
            }.show()
    }
}