package com.example.quikcart.ui.productdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quikcart.databinding.FragmentProductDetailsBinding
import com.example.quikcart.models.entities.ProductsItem

class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailsBinding
    private var productItem: ProductsItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productItem = arguments?.getSerializable("details") as? ProductsItem
        Log.i("TAG", "onViewCreated: $productItem")
    }
}
