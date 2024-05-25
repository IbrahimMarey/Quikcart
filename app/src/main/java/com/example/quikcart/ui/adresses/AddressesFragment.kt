package com.example.quikcart.ui.adresses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.quikcart.databinding.FragmentAddressesBinding


class AddressesFragment : Fragment() {
    lateinit var binding:FragmentAddressesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addAddressBtn.setOnClickListener {
            val action = AddressesFragmentDirections.actionAddressesFragmentToMapFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
}