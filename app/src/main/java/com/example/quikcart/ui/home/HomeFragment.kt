package com.example.quikcart.ui.home

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.ClipboardManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentHomeBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.CategoryItem
import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.utils.AlertUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var brandAdapter : BrandAdapter
    private lateinit var categoryAdapter: CategoryAdapter
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
        initCategoryRecyclerView()
        ObserveOnCouponLiveData()
       // initImageSlider()
        binding.searchBar.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }



    private fun initCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter{
           navigateToProductsFragment(0,it,false)
        }
        categoryAdapter.submitList(viewModel.getCategories())
        binding.recyclerCategories.adapter = categoryAdapter
    }

    private fun initBrandsRecyclerView(brands:List<SmartCollectionsItem>) {
        brandAdapter = BrandAdapter {brand->
            brand.id?.let { navigateToProductsFragment(it,null,true) }
        }
        brandAdapter.submitList(brands)
        binding.recyclerBrands.adapter = brandAdapter
    }
    private fun navigateToProductsFragment(brandId: Long, categoryItem: CategoryItem?, isBrand:Boolean){
        val action = HomeFragmentDirections.actionHomeFragmentToProductFragment(brandId,isBrand,categoryItem)
        findNavController().navigate(action)
    }

    private fun observeOnStateFlow() {
        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is ViewState.Error ->{
                            binding.brandsProgressbar.visibility = View.GONE
                            AlertUtil.showSnackbar(requireView(), it.message)}
                        is ViewState.Success -> {
                            initBrandsRecyclerView(it.data)
                            binding.brandsProgressbar.visibility = View.GONE
                            initImageSlider(viewModel.couponsList.value ?: emptyList())

                            // initImageSlider()
                        }
                        is ViewState.Loading -> binding.brandsProgressbar.visibility = View.VISIBLE

                    }
                }
            }
        }
    }

    private fun ObserveOnCouponLiveData()
    {
        viewModel.couponsList.observe(viewLifecycleOwner) { coupons ->
            initImageSlider(coupons)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    /*private fun initImageSlider()
    {
        binding.imageSlider.setImageList(viewModel.couponsList,ScaleTypes.FIT)
        binding.imageSlider.startSliding(2000)
        binding.imageSlider.setItemClickListener(object : ItemClickListener{
            override fun doubleClick(position: Int) {

            }
            override fun onItemSelected(position: Int) {
                copyCoupon(position)
            }
        })
    }*/



    private fun initImageSlider(couponsList: List<SlideModel>) {
        binding.imageSlider.setImageList(couponsList, ScaleTypes.FIT)
        binding.imageSlider.startSliding(2000)
        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {}

            override fun onItemSelected(position: Int) {
                copyCoupon(position)
            }
        })
    }

    private fun copyCoupon(index: Int) {
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = viewModel.couponsIDs[index]
        } else {
            val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("text", viewModel.couponsIDs[index])
            clipboard.setPrimaryClip(clip)
        }
    }
}