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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter
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
        getProducts()
        observeViewModel()
    }
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ViewState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is ViewState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            setupRecyclerView(state.data)
                            binding.rvFavorite.adapter = adapter
                            Log.i("SearchFragment", "Products: ${state.data}.count()")
                        }
                        is ViewState.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
    private fun getProducts() {
        viewModel.getProducts()
    }

    private fun setupRecyclerView(products: List<ProductsItem>) {
        adapter = FavoriteAdapter(
            { productItem -> navigateToProductDetails(productItem) },
            {productItem -> viewModel.deleteProduct(productItem) }
        )
        binding.rvFavorite.adapter = adapter
        adapter.submitList(products)
    }
    private fun navigateToProductDetails(productItem: ProductsItem) {
        val bundle = Bundle().apply {
            putSerializable("details", productItem)
        }
        findNavController().navigate(R.id.action_favoriteFragment_to_productDetailsFragment, bundle)
    }


}