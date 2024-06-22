package com.example.quikcart.ui.productdetails

import android.app.AlertDialog
import android.content.Intent
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
import com.example.quikcart.models.entities.cart.CartCustomer
import com.example.quikcart.models.entities.cart.DraftItem
import com.example.quikcart.models.entities.cart.DraftOrderLineItem
import com.example.quikcart.models.entities.cart.PostDraftOrderItemModel
import com.example.quikcart.models.entities.cart.PutDraftItem
import com.example.quikcart.models.entities.cart.PutDraftOrderItemModel
import com.example.quikcart.ui.authentication.AuthenticationActivity
import com.example.quikcart.utils.PreferencesUtils
import com.example.quikcart.utils.setPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var productItem: ProductsItem
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
        val favID = pref.getFavouriteId()

        viewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]
        productItem = ProductDetailsFragmentArgs.fromBundle(requireArguments()).product
//        productItem = arguments?.getSerializable("details") as ProductsItem
/*
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            binding.networkImageView.visibility=View.GONE
            binding.scrollView.visibility=View.VISIBLE

        } else {
            setPlaceholderImage()
        }*/
        productItem?.let {
            binding.product = it
            setImages(it.images)
            setVariants(it.variants)
            binding.price.setPrice (it.variants?.get(0)?.price?.toFloat() ?: 0.0f, requireContext())
        }
        checkProduct(productItem)
        binding.addToFavorite.setOnClickListener {
            if(pref.getUserId()!="0"&&pref.getUserId()!="-1"){
                insertToFavorite(productItem , favID)
                binding.addToFavorite.setImageResource(R.drawable.ic_heart)
            }
            else {
                showSignInAlert("favourites")
            }
        }
        productItem.productType?.let { showReview(it) }
        binding.rateOfProductDetails.rating = 4.7f
        if (cartID.toInt() != 0 && cartID.toInt() != -1) {
            viewModel.getCart(cartID.toString())
        }
        binding.editProductBtn.setOnClickListener {
            if(pref.getUserId()!="0"&&pref.getUserId()!="-1"){

            if (cartID.toInt() == 0) {
                val item = PostDraftOrderItemModel(
                    DraftItem(
                        line_items = listOf(DraftOrderLineItem(productItem?.title ?: "", productItem?.price ?: "", 1)),
                        applied_discount = null,
                        customer = CartCustomer(pref.getCustomerId()),
                    )
                )

                lifecycleScope.launch {
                    pref.setCartId(viewModel.postProductInCart(item))
                }
            } else {
                val data = PutDraftItem(viewModel.getItemLineList(productItem?.title ?: "", productItem?.price ?: ""))
                val request = PutDraftOrderItemModel(data)
                viewModel.putProductInCart(cartID.toString(), request)
            }
            }
            else {
                showSignInAlert("cart")
            }

        }
    }

    private fun checkProduct(productItem: ProductsItem){
        if(productItem.isFavorited){
            binding.addToFavorite.setImageResource(R.drawable.ic_heart)
        } else {
            binding.addToFavorite.setImageResource(R.drawable.ic_empty_heart)
        }
    }
    private fun setPlaceholderImage() {
        binding.networkImageView.visibility=View.VISIBLE
        binding.scrollView.visibility=View.GONE
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
    private fun insertToFavorite(productsItem: ProductsItem , favID:Long){
        var product:ProductsItem =productsItem
        product.isFavorited=true
        viewModel.insertToFavourites(product)
        val price = productItem?.price ?: "0.00"
        val title = productItem?.title ?: ""
        if (favID.toInt() != 0 && favID.toInt() != -1)
            viewModel.getFav(favID.toString())
        if (favID.toInt() == 0) {
            val draftItem = PostDraftOrderItemModel(
                DraftItem(
                    line_items = listOf(DraftOrderLineItem(title, price, 1)),
                    applied_discount = null,
                    customer = CartCustomer(pref.getCustomerId())
                )
            )
            lifecycleScope.launch {
                pref.setCartId(viewModel.postProductInFav(draftItem))
            }
        } else {
            val draftItem = PutDraftItem(viewModel.getItemLineList(title, price))
            val request = PutDraftOrderItemModel(draftItem)
            viewModel.putProductInFav(favID.toString(), request)
        }
    }
    private fun showSignInAlert(message: String) {
        AlertDialog.Builder(activity)
            .setTitle("Please Sign In")
            .setMessage("You need to sign in to add items to your $message.")
            .setPositiveButton("Sign In") { dialog, _ ->
                startActivity(Intent(activity, AuthenticationActivity::class.java))
//                requireActivity().finish()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}