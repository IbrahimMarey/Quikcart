package com.example.quikcart.ui.placeorder.secondscreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quikcart.R
class PlaceOrderFragment : Fragment() {

    private lateinit var totalPrice : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalPrice = PlaceOrderFragmentArgs.fromBundle(requireArguments()).totalPrice
        Log.i("TAG", "PlaceOrderFragment Total Price = = = = = = = = = = $totalPrice ")
    }

}