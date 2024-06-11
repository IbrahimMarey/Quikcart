package com.example.quikcart.ui.search

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
import androidx.navigation.fragment.findNavController
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentSearchBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.cart.*
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

        setupUI()
        observeViewModel()
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

            binding.emptyImageView.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
            binding.productRecyclerView.visibility = if (filteredList.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun initRecyclerView(products: List<ProductsItem>) {
        adapter = SearchAdapter(
            { productItem -> navigateToProductDetails(productItem) },
            { productItem -> addToFavorite(productItem) }
        )
        binding.productRecyclerView.adapter = adapter
        adapter.submitList(products)
    }

    private fun navigateToProductDetails(productItem: ProductsItem) {
        val bundle = Bundle().apply {
            putSerializable("details", productItem)
        }
        findNavController().navigate(R.id.action_searchFragment_to_productDetailsFragment, bundle)
    }

    private fun addToFavorite(productItem: ProductsItem) {
        viewModel.addToFavourites(productItem)

        val price = productItem.price ?: "0.00"
        val title = productItem.title ?: ""
        if (favID.toInt() != 0 && favID.toInt() != -1)
            viewModel.getFav(favID.toString())
        if (favID.toInt() == 0) {
            val draftItem = PostDraftOrderItemModel(
                DraftItem(
                    line_items = listOf(DraftOrderLineItem(title, price, 1)),
                    applied_discount = null,
                    customer = CartCustomer(preferences.getCustomerId())
                )
            )

            lifecycleScope.launch {
                preferences.setCartId(viewModel.postProductInFav(draftItem))
            }
        } else {
            val draftItem = PutDraftItem(viewModel.getItemLineList(title, price))
            val request = PutDraftOrderItemModel(draftItem)
            viewModel.putProductInFav(favID.toString(), request)
        }
    }
}
