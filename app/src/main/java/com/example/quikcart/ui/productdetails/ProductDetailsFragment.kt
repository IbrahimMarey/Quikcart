package com.example.quikcart.ui.productdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quikcart.databinding.FragmentProductDetailsBinding
import com.example.quikcart.models.entities.ImagesItem
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.VariantsItem
import com.example.quikcart.models.entities.cart.CartAppliedDiscount
import com.example.quikcart.models.entities.cart.CartCustomer
import com.example.quikcart.models.entities.cart.CartItem
import com.example.quikcart.models.entities.cart.CartLineItems
import com.example.quikcart.models.entities.cart.PostCartItemModel
import com.example.quikcart.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var binding: FragmentProductDetailsBinding
    private var productItem: ProductsItem? = null
    private lateinit var imageAdapter: ImagesAdapter
    private lateinit var variantAdapter: VariantsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]
        productItem = arguments?.getSerializable("details") as? ProductsItem
        productItem?.let {
            binding.product = it
            setImages(it.images)
            setVariants(it.variants)
        }
        binding.rateOfProductDetails.rating = 4.7f
        binding.editProductBtn.setOnClickListener{

            Log.i("TAG", "onViewCreated:========================= ${productItem?.image?.src}")
            val item =PostCartItemModel(
                CartItem(
                    name = productItem?.image?.src ?: "https://www.shutterstock.com/image-vector/shopping-cart-icon-vector-illustration-600nw-1726574749.jpg",
                    line_items = listOf(CartLineItems(productItem?.title?: "",productItem?.price?:"",1)),
                    applied_discount = CartAppliedDiscount(description = productItem?.image?.src ?: "https://www.shutterstock.com/image-vector/shopping-cart-icon-vector-illustration-600nw-1726574749.jpg",null,null,null,null),
                    customer = CartCustomer(
                        PreferencesUtils.getInstance(requireActivity()).getUserId()?.toLong()?:7406457553131),
            ))
            viewModel.postProductInCart(item)
        }
    }

    private fun setVariants(variants: List<VariantsItem>?) {
        variantAdapter= VariantsAdapter()
        binding.recyclerViewColors.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = variantAdapter
        }
        variants?.let {
            variantAdapter.submitList(it)
        }
    }
    private fun setImages(images: List<ImagesItem>?) {
        imageAdapter = ImagesAdapter()
        binding.recyclerViewImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = imageAdapter
        }
        images?.let {
            imageAdapter.submitList(it)
        }
    }


}
