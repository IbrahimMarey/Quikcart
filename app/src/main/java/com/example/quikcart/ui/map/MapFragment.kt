package com.example.quikcart.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentMapBinding
import com.example.quikcart.models.entities.AddressModel
import com.example.quikcart.ui.adresses.AddressesViewModel
import com.example.quikcart.utils.getMarkerAddress
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding : FragmentMapBinding
    private lateinit var map: GoogleMap
    private var selectedLatLng = LatLng(30.616689, 32.274014)
    private lateinit var materialAboutUsBuilder: MaterialAlertDialogBuilder
    private lateinit var addressViewModel: AddressesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater,container,false)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        materialAboutUsBuilder = MaterialAlertDialogBuilder(requireActivity())
        addressViewModel = ViewModelProvider(this)[AddressesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getLocationBtn.setOnClickListener{
            showLocationDialog()
        }
    }

    private fun showLocationDialog(){
        val addressData = layoutInflater.inflate(R.layout.get_title_and_phone_dialog, null)

        val alertDialog = materialAboutUsBuilder.setView(addressData)
            .setBackground(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.dialogue_background, requireActivity().theme
                )
            ).setCancelable(true).show()

        addressData.findViewById<Button>(R.id.confirmBtn).setOnClickListener {

            confirmLocationDialog(alertDialog,
                addressData.findViewById<EditText>(R.id.locationTitleInput).text.toString(),
                addressData.findViewById<EditText>(R.id.locationPhoneInput).text.toString()
            )
        }
        addressData.findViewById<Button>(R.id.dismissBtn).setOnClickListener {
            alertDialog.cancel()
        }
    }

    private fun confirmLocationDialog(alert : AlertDialog,title:String,phone:String){
        if (checkStringLength(title)&&checkStringLength(phone))
        {
            val addressModel = AddressModel(title,phone,selectedLatLng.latitude,selectedLatLng.longitude)
            addressViewModel.insertAddress(addressModel)
            alert.cancel()
            Navigation.findNavController(requireView()).navigateUp()
        }
    }

    private fun checkStringLength(input: String):Boolean {
        if (input.length < 4) {
            Toast.makeText(context, "Enter Valid Data", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val currentLocation = LatLng(30.616689, 32.274014)
        googleMap.addMarker(MarkerOptions().position(currentLocation).title("Your Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        binding.mapView.getMapAsync(){
            it.addMarker(MarkerOptions().position(currentLocation).title("Your Location"))
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        }
        map.setOnMapClickListener { latLng ->
            // Move the camera to the clicked position
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

            // Clear existing markers (if any)
            googleMap.clear()
            selectedLatLng=latLng
            // Add a marker at the clicked position
            googleMap.addMarker(MarkerOptions().position(latLng).title(getMarkerAddress(requireActivity(),map.cameraPosition.target.latitude, map.cameraPosition.target.longitude)).snippet("Marker Snippet"))
        }/*
        map.setOnCameraMoveListener {
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLng(
                LatLng(
                    map.cameraPosition.target.latitude, map.cameraPosition.target.longitude
                )
            ))

            // Clear existing markers (if any)
            googleMap.clear()
            selectedLatLng =LatLng(
                map.cameraPosition.target.latitude, map.cameraPosition.target.longitude
            )
            // Add a marker at the clicked position
            googleMap.addMarker(
                MarkerOptions().position(
                LatLng(
                    map.cameraPosition.target.latitude, map.cameraPosition.target.longitude
                )
                //getAddressEnglish(requireActivity(),map.cameraPosition.target.latitude, map.cameraPosition.target.longitude)
            ).title("location").snippet("Marker Snippet"))
        }*/
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}