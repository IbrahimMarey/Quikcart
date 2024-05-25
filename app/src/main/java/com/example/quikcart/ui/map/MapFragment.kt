package com.example.quikcart.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentMapBinding
import com.example.quikcart.helpers.getMarkerAddress
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding : FragmentMapBinding
    private lateinit var map: GoogleMap
    private var selectedLatLng = LatLng(30.616689, 32.274014)
    /*private lateinit var mapViewModel: MapViewModel
    private lateinit var mapViewModelFactory: MapViewModelFactory*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater,container,false)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getLocationBtn.setOnClickListener{

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
        }
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