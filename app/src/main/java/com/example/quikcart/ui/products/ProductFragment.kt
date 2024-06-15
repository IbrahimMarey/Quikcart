package com.example.quikcart.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentProductBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.CategoryItem
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.cart.CartCustomer
import com.example.quikcart.models.entities.cart.DraftItem
import com.example.quikcart.models.entities.cart.DraftOrderLineItem
import com.example.quikcart.models.entities.cart.PostDraftOrderItemModel
import com.example.quikcart.models.entities.cart.PutDraftItem
import com.example.quikcart.models.entities.cart.PutDraftOrderItemModel
import com.example.quikcart.utils.AlertUtil
import com.example.quikcart.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class ProductFragment : Fragment() {
    private lateinit var adapter : ProductAdapter
    private lateinit var binding: FragmentProductBinding
    private lateinit var productsViewModel: ProductsViewModel
    private var brandId:Long?=null
    private var categoryItem:CategoryItem?=null
    private lateinit var preferences: PreferencesUtils
    private var favID by Delegates.notNull<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        binding.lifecycleOwner=this
        binding.vm=productsViewModel
        brandId = ProductFragmentArgs.fromBundle(requireArguments()).brandId
        categoryItem = ProductFragmentArgs.fromBundle(requireArguments()).categoryItem
        val isBrand = ProductFragmentArgs.fromBundle(requireArguments()).isBrands
        preferences = PreferencesUtils.getInstance(requireActivity())
        favID = preferences.getFavouriteId()
        getProducts(isBrand)
        observeOnStateFlow()
    }

    private fun getProducts(isBrand: Boolean) {
        if(isBrand){
            brandId?.let { getProductsByBrand(it) }
        }else {
            getProductsByCategory(categoryItem?.name)
        }

    }


    private fun getProductsByCategory(category: String?) {
        binding.chipGroup.visibility=View.VISIBLE
        category?.let { productsViewModel.getProductsBySubCategory(it, "ACCESSORIES") }

        binding.chipShirt.setOnClickListener {
            category?.let { productsViewModel.getProductsBySubCategory(it, "T-SHIRTS") }
        }
        binding.chipShoes.setOnClickListener {
            category?.let { productsViewModel.getProductsBySubCategory(it, "SHOES") }
        }
        binding.chipAccessories.setOnClickListener {
            category?.let { productsViewModel.getProductsBySubCategory(it, "ACCESSORIES") }
        }

    }


    private fun getProductsByBrand(brandId:Long) {
        productsViewModel.getProductsByBrandId(brandId)
    }



    private fun observeOnStateFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsViewModel.uiState.collect {
                    when (it) {
                        is ViewState.Error ->{
                            binding.progressBar.visibility = View.GONE
                            AlertUtil.showToast(requireContext(), it.message)
                        }
                        is ViewState.Success -> {
                            binding.noProductImg.visibility= if(it.data.isEmpty()) View.VISIBLE else View.GONE
                            initRecyclerView(it.data)
                           binding.progressBar.visibility = View.GONE
                        }
                        is ViewState.Loading ->
                            binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun initRecyclerView(products: List<ProductsItem>) {
        adapter = ProductAdapter(
            { productItem -> navigateToProductDetails(productItem) },
            { productItem -> addToFavorite(productItem) }
        )
        binding.recyclerProducts.adapter = adapter
        adapter.submitList(products)
    }
    private fun initViewModel() {
        productsViewModel = ViewModelProvider(this)[ProductsViewModel::class.java]
    }
    private fun navigateToProductDetails(productItem: ProductsItem) {
        val bundle = Bundle().apply {
            putSerializable("details", productItem)
        }
        findNavController().navigate(R.id.action_productFragment_to_productDetailsFragment, bundle)
    }

    private fun addToFavorite(productItem: ProductsItem) {
        productsViewModel.addToFavourites(productItem)

        val price = productItem.price ?: "0.00"
        val title = productItem.title ?: ""
        if (favID.toInt() != 0 && favID.toInt() != -1)
            productsViewModel.getFav(favID.toString())
        if (favID.toInt() == 0) {
            val draftItem = PostDraftOrderItemModel(
                DraftItem(
                    line_items = listOf(DraftOrderLineItem(title, price, 1)),
                    applied_discount = null,
                    customer = CartCustomer(preferences.getCustomerId())
                )
            )

            lifecycleScope.launch {
                preferences.setCartId(productsViewModel.postProductInFav(draftItem))
            }
        } else {
            val draftItem = PutDraftItem(productsViewModel.getItemLineList(title, price))
            val request = PutDraftOrderItemModel(draftItem)
            productsViewModel.putProductInFav(favID.toString(), request)
        }
    }
}