package com.example.quikcart.ui.productdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentProductDetailsBinding
import com.example.quikcart.models.entities.ImagesItem
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.VariantsItem
import com.example.quikcart.models.entities.cart.CartAppliedDiscount
import com.example.quikcart.models.entities.cart.CartCustomer
import com.example.quikcart.models.entities.cart.DraftItem
import com.example.quikcart.models.entities.cart.DraftOrderLineItem
import com.example.quikcart.models.entities.cart.PostDraftOrderItemModel
import com.example.quikcart.models.entities.cart.PutDraftItem
import com.example.quikcart.models.entities.cart.PutDraftOrderItemModel
import com.example.quikcart.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var binding: FragmentProductDetailsBinding
    private var productItem: ProductsItem? = null
    private lateinit var imageAdapter: ImagesAdapter
    private lateinit var variantAdapter: VariantsAdapter
    private lateinit var pref : PreferencesUtils
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = PreferencesUtils.getInstance(requireActivity())
        val cartID = pref.getCartId()
        viewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]
        productItem = arguments?.getSerializable("details") as? ProductsItem
        productItem?.let {
            binding.product = it
            setImages(it.images)
            setVariants(it.variants)
        }
        binding.rateOfProductDetails.rating = 4.7f
        binding.editProductBtn.setOnClickListener{
            if (cartID == 0.toLong())
            {
                val item = PostDraftOrderItemModel(
                    DraftItem(
                        line_items = listOf(DraftOrderLineItem(productItem?.title?: "",productItem?.price?:"",1)),
                        applied_discount = CartAppliedDiscount(description = productItem?.image?.src ?: "https://www.shutterstock.com/image-vector/shopping-cart-icon-vector-illustration-600nw-1726574749.jpg",null,null,null,null),
                        customer = CartCustomer(
                            PreferencesUtils.getInstance(requireActivity()).getUserId()?.toLong()?:7406457553131),
                    )
                )

                lifecycleScope.launch{
                    pref.setCartId(viewModel.postProductInCart(item))
                }
            }else{
                var data =
                    PutDraftItem(viewModel.getItemLineList(productItem?.title?: "",productItem?.price?:""))
                var request = PutDraftOrderItemModel(data)
                viewModel.putProductInCart(cartID.toString(),request)
            }

        }
    }
   private fun showReview(type: String) {
       binding.reviews.setOnClickListener {
           navigateToProductDetails(type)
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

    private fun navigateToProductDetails(type: String) {
        val bundle = Bundle().apply {
            putSerializable("productType", type)
        }
        findNavController().navigate(R.id.action_productDetailsFragment_to_reviewFragment, bundle)
    }
}
