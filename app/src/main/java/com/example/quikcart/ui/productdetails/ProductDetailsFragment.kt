package com.example.quikcart.ui.productdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quikcart.databinding.FragmentProductDetailsBinding
import com.example.quikcart.models.entities.ImagesItem
import com.example.quikcart.models.entities.ProductsItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var binding: FragmentProductDetailsBinding
    private var productItem: ProductsItem? = null
    private lateinit var imageAdapter: ImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]
        productItem = arguments?.getSerializable("details") as? ProductsItem
        Log.i("TAG", "onViewCreated: $productItem")
        productItem?.let {
            binding.product = it
            setImages(it.images)
        }
        binding.rateOfProductDetails.rating = 4.7f
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
}
