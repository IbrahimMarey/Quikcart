package com.example.quikcart.ui.home

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
import com.example.quikcart.databinding.FragmentHomeBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.utils.AlertUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var brandAdapter : BrandAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observeOnStateFlow()



    }

    private fun initBrandsRecyclerView(brands:List<SmartCollectionsItem>) {
        brandAdapter = BrandAdapter {brand->
            brand.id?.let { navigateToProductsFragment(it) }
        }
        brandAdapter.submitList(brands)
        binding.recyclerBrands.adapter = brandAdapter
    }
    private fun navigateToProductsFragment(brandId:Long){
        val action = HomeFragmentDirections.actionHomeFragmentToProductFragment(brandId)
        findNavController().navigate(action)
    }

    private fun observeOnStateFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is ViewState.Error ->{
                            binding.brandsProgressbar.visibility = View.GONE
                            AlertUtil.showToast(requireContext(), it.message)}
                        is ViewState.Success -> {
                            initBrandsRecyclerView(it.data)
                            binding.brandsProgressbar.visibility = View.GONE
                        }
                        is ViewState.Loading -> binding.brandsProgressbar.visibility = View.VISIBLE

                    }
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }


}