package com.example.quikcart.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentMapBinding
import com.example.quikcart.databinding.GetTitleAndPhoneDialogBinding
import com.example.quikcart.models.entities.AddressModel
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.PostAddressBodyModel
import com.example.quikcart.models.entities.PostAddressModel
import com.example.quikcart.ui.adresses.AddressesViewModel
import com.example.quikcart.utils.PreferencesUtils
import com.example.quikcart.utils.getMarkerAddress
import com.example.quikcart.utils.isEgyptianPhoneNumberValid
import com.example.quikcart.utils.isLocationInEgypt
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import java.util.concurrent.atomic.AtomicLong

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
        var postadderssbody = PostAddressBodyModel(",","","","","","","","","","","","","","")
        val addressData = layoutInflater.inflate(R.layout.get_title_and_phone_dialog, null)
        var isNewPhone = false
        val alertDialog = materialAboutUsBuilder.setView(addressData)
            .setBackground(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.dialogue_background, requireActivity().theme
                )
            ).setCancelable(true).show()
        addressData.findViewById<EditText>(R.id.locationPhoneInput).visibility =View.GONE

        addressData.findViewById<RadioButton>(R.id.chooseDefaultPhone).toggle()
        addressData.findViewById<RadioGroup>(R.id.choosePhoneGroup).setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.chooseDefaultPhone->{
                    addressData.findViewById<EditText>(R.id.locationPhoneInput).visibility =View.GONE
                    isNewPhone = false
                }
                R.id.chooseNewPhone->{
                    addressData.findViewById<EditText>(R.id.locationPhoneInput).visibility =View.VISIBLE
                    isNewPhone = true
                }
            }
        }

        addressData.findViewById<Button>(R.id.confirmBtn).setOnClickListener {
            if (isNewPhone){
                if (isEgyptianPhoneNumberValid(addressData.findViewById<EditText>(R.id.locationPhoneInput).text.toString()))
                {
                    postadderssbody.address1 = addressData.findViewById<EditText>(R.id.locationTitleInput).text.toString()
                    postadderssbody.address2 = getMarkerAddress(requireContext(),selectedLatLng.latitude, selectedLatLng.longitude)
                    postadderssbody.phone = addressData.findViewById<EditText>(R.id.locationPhoneInput).text.toString()
                    confirmLocationDialog(PostAddressModel(postadderssbody),alertDialog)
                }else
                {
                    Toast.makeText(context, getString(R.string.phone_miss_match), Toast.LENGTH_SHORT).show()
                }
            }else{
                postadderssbody.address1 = addressData.findViewById<EditText>(R.id.locationTitleInput).text.toString()
                postadderssbody.address2 = getMarkerAddress(requireActivity(),selectedLatLng.latitude, selectedLatLng.longitude)
                confirmLocationDialog(PostAddressModel(postadderssbody),alertDialog)
            }

        }
        addressData.findViewById<Button>(R.id.dismissBtn).setOnClickListener {
            alertDialog.cancel()
        }
    }

    private fun confirmLocationDialog(address:PostAddressModel,alert:AlertDialog){

        addressViewModel.postAddress(
            PreferencesUtils.getInstance(requireContext()).getUserId()
            ?.toLong()?: "7406457553131".toLong(),address)
        alert.cancel()
        Navigation.findNavController(requireView()).navigateUp()
    }

    private fun checkStringLength(input: String):Boolean {
        if (input.length < 4) {
            Toast.makeText(context, "Enter Valid Address Title", Toast.LENGTH_SHORT).show()
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
        binding.mapView.getMapAsync{
            it.addMarker(MarkerOptions().position(currentLocation).title("Your Location"))
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        }
        map.setOnMapClickListener { latLng ->
            if (isLocationInEgypt(latLng.latitude, latLng.longitude)) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                googleMap.clear()
                selectedLatLng=latLng
                googleMap.addMarker(MarkerOptions().position(latLng).title(getMarkerAddress(requireActivity(),map.cameraPosition.target.latitude, map.cameraPosition.target.longitude)).snippet("Marker Snippet"))
            } else {
                Toast.makeText(context, R.string.not_allow_location, Toast.LENGTH_SHORT).show()
            }
        }
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