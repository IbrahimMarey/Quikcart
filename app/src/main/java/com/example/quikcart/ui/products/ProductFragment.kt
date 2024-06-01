package com.example.quikcart.ui.products

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
import com.example.quikcart.databinding.FragmentProductBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.utils.AlertUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {
    private lateinit var adapter : ProductAdapter
    private lateinit var binding: FragmentProductBinding
    private lateinit var productsViewModel: ProductsViewModel


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
        val brandId = ProductFragmentArgs.fromBundle(requireArguments()).brandId
        Log.e("TAG", "onViewCreated:${brandId} ", )
        productsViewModel.getProductsByBrandId(brandId)
        observeOnStateFlow()

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
        adapter = ProductAdapter { productItem ->
            navigateToProductDetails(productItem)
        }
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
}