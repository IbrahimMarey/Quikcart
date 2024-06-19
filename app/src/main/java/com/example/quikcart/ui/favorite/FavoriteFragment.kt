package com.example.quikcart.ui.favorite

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
import com.example.quikcart.databinding.FragmentFavoriteBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.cart.LineItem
import com.example.quikcart.utils.PreferencesUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter
    private lateinit var draftOrderViewModel: DraftOrderViewModel
    private lateinit var preferences: PreferencesUtils
    private var favID by Delegates.notNull<Long>()
    private lateinit var lineItem: MutableList<LineItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        draftOrderViewModel = ViewModelProvider(this)[DraftOrderViewModel::class.java]
        viewModel.getProducts()
        preferences = PreferencesUtils.getInstance(requireActivity())
        favID = preferences.getFavouriteId()
        lineItem= draftOrderViewModel.lineItemsList
        observeViewModel()
    }
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleFavoriteProductsState(state)
                }
            }
        }
    }

    private fun handleFavoriteProductsState(state: ViewState<List<ProductsItem>>) {
        when (state) {
            is ViewState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is ViewState.Success -> {
                binding.progressBar.visibility = View.GONE
                if (state.data.isEmpty()) {
                    binding.emptyImageView.visibility = View.VISIBLE
                    binding.rvFavorite.visibility = View.GONE
                } else {
                    binding.emptyImageView.visibility = View.GONE
                    binding.rvFavorite.visibility = View.VISIBLE
                    setupRecyclerView(state.data)
                }
                Log.i("FavoriteFragment", "Products: ${state.data.count()}")
            }
            is ViewState.Error -> {
                binding.progressBar.visibility = View.GONE
                Snackbar.make(requireView(), state.message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRecyclerView(products: List<ProductsItem>) {
        adapter = FavoriteAdapter(
            { productItem -> navigateToProductDetails(productItem) },
            { productItem -> deleteProduct(productItem) }
        )
        binding.rvFavorite.adapter = adapter
        adapter.submitList(products)
    }

    private fun deleteProduct(productItem: ProductsItem) {
        Snackbar.make(
            requireView(),
            getString(R.string.are_you_sure_you_want_to_delete_this_item_from_fav),
            Snackbar.LENGTH_LONG
        )
            .setAction(getString(R.string.delete)) {

                viewModel.deleteProduct(productItem)
                deleteProductFromFavorites(productItem)
            }.show()
    }
    private fun deleteProductFromFavorites(productItem: ProductsItem ) {
        val favId = preferences.getFavouriteId().toString()
         lifecycleScope.launch {
              try {
                  val matchingLineItem = draftOrderViewModel.lineItemsList.find { it.title == productItem.title }
                  if (matchingLineItem != null) {
                       if (draftOrderViewModel.lineItemsList.size >= 2) {
                           draftOrderViewModel.delFavItem(favId, matchingLineItem)
                           deleteProduct(productItem)
                            } else {
                                draftOrderViewModel.delFav(favId)
                                preferences.setFavouriteId(0)
                                deleteProduct(productItem)
                            }
                        } else {
                            Log.i("TAG", "No matching line item found for product: ${productItem.title}")
                        }
              }
              catch (e: Exception) {
                  Log.i("TAG", "deleteProductFromFavorites: $e")
              }
         }
    }

    private fun navigateToProductDetails(productItem: ProductsItem) {
        val bundle = Bundle().apply {
            putSerializable("details", productItem)
        }
        findNavController().navigate(R.id.action_favoriteFragment_to_productDetailsFragment, bundle)
    }
}
