package com.example.quikcart.ui.search

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentSearchBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.cart.*
import com.example.quikcart.ui.authentication.AuthenticationActivity
import com.example.quikcart.utils.AlertUtil
import com.example.quikcart.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter
    private var productList = mutableListOf<ProductsItem>()
    private var filteredList = mutableListOf<ProductsItem>()
    private lateinit var preferences: PreferencesUtils
    private var favID by Delegates.notNull<Long>()
    private var counter=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        preferences = PreferencesUtils.getInstance(requireActivity())
        favID = preferences.getFavouriteId()
        binding.vm = viewModel
        setupUI()
        observeViewModel()
        checkPriceSliderVisibility()


    }

    private fun checkPriceSliderVisibility() {
        binding.filterTv.setOnClickListener {
            counter++
            Log.e("TAG", "onViewCreated1: ${counter}", )
            binding.sliderLinear.visibility = if (counter % 2 == 0) View.GONE else View.VISIBLE
        }
    }

    private fun setupUI() {
        getProducts()
        setupSearchBar()
    }

    private fun getProducts() {
        viewModel.getProducts()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ViewState.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is ViewState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            productList.clear()
                            productList.addAll(state.data)
                            initRecyclerView(productList)
                            Log.i("SearchFragment", "Products: ${productList.count()}")
                        }
                        is ViewState.Error -> binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupSearchBar() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterProducts(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterProducts(it) }
                return true
            }
        })
    }

    private fun filterProducts(query: String) {
        lifecycleScope.launch {
            filteredList.clear()
            filteredList.addAll(productList.filter {
                it.title!!.contains(query, ignoreCase = true) || it.productType!!.contains(query, ignoreCase = true)
            })
            adapter.submitList(filteredList.toList())
            viewModel.originalProducts=filteredList

            binding.emptyImageView.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
            binding.productRecyclerView.visibility = if (filteredList.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun initRecyclerView(products: List<ProductsItem>) {
        adapter = SearchAdapter(
            { productItem -> navigateToProductDetails(productItem) },
            { productItem, position -> addToFavorite(productItem, position) }
        )
        binding.productRecyclerView.adapter = adapter
        adapter.submitList(products)
    }


    private fun navigateToProductDetails(productItem: ProductsItem) {
        val action = SearchFragmentDirections.actionSearchFragmentToProductDetailsFragment(productItem)
        findNavController().navigate(action)
//        val bundle = Bundle().apply {
//            putSerializable("details", productItem)
//        }
//        findNavController().navigate(R.id.action_searchFragment_to_productDetailsFragment, bundle)
    }

    private fun addToFavorite(productItem: ProductsItem, position: Int) {
        if(preferences.getUserId()!="0"&&preferences.getUserId()!="-1") {
            viewModel.addToFavourites(productItem)
            val price = "0.00"
            val title = productItem.title ?: ""

            if (favID.toInt() != 0 && favID.toInt() != -1) {
                viewModel.getFav(favID.toString())
                lifecycleScope.launch {
                    val draftItem = PutDraftItem(viewModel.getItemLineList(title, price))
                    val request = PutDraftOrderItemModel(draftItem)
                    viewModel.putProductInFav(favID.toString(), request)
                    handleSuccess(productItem, position)
                }
            } else {
                val draftItem = PostDraftOrderItemModel(
                    DraftItem(
                        line_items = listOf(DraftOrderLineItem(title, price, 1)),
                        applied_discount = null,
                        customer = CartCustomer(preferences.getCustomerId())
                    )
                )

                lifecycleScope.launch {
                    favID = viewModel.postProductInFav(draftItem)
                    preferences.setFavouriteId(favID)
                    handleSuccess(productItem, position)
                }
            }
        }
        else{
            showSignInAlert()
        }
    }

    private fun handleSuccess(productItem: ProductsItem, position: Int) {
        productItem.isFavorited = true
        adapter.notifyItemChanged(position)
        AlertUtil.showSnackbar(requireView(),"Product added to favorites successfully!")
    }
    private fun showSignInAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle("Please Sign In")
            .setMessage("You need to sign in to add items to your favorites.")
            .setPositiveButton("Sign In") { dialog, _ ->
                startActivity(Intent(requireContext(), AuthenticationActivity::class.java))
                requireActivity().finish()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
