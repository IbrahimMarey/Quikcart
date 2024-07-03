package com.example.quikcart.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import com.example.quikcart.R
import com.example.quikcart.databinding.AboutUsDialogBinding
import com.example.quikcart.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private lateinit var materialAboutUsBuilder: MaterialAlertDialogBuilder
    private lateinit var aboutUsDialog:AboutUsDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        aboutUsDialog = AboutUsDialogBinding.inflate(LayoutInflater.from(container?.context), null, false)
        materialAboutUsBuilder = MaterialAlertDialogBuilder(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addressSittings.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToAddressesFragment()
            Navigation.findNavController(it).navigate(R.id.addressesFragment)
        }
        binding.contactUs.setOnClickListener{
            contactUsDialog()
        }
        binding.aboutUs.setOnClickListener{
            aboutUsDialogOpening()
        }
    }

    private fun contactUsDialog(){
        val contactUs = layoutInflater.inflate(R.layout.contcat_us_dialog, null)

        val alertDialog = materialAboutUsBuilder.setView(contactUs)
            .setBackground(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.dialogue_background, requireActivity().theme
                )
            ).setCancelable(true).show()

        contactUs.findViewById<Button>(R.id.contact_us_dismiss).setOnClickListener {
            alertDialog.cancel()
        }

    }
    private fun aboutUsDialogOpening() {

        val aboutUsView = layoutInflater.inflate(R.layout.about_us_dialog, null)

        val alertDialog = materialAboutUsBuilder.setView(aboutUsView)
            .setBackground(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.dialogue_background, requireActivity().theme
                )
            ).setCancelable(true).show()

        aboutUsView.findViewById<Button>(R.id.button_dismiss).setOnClickListener {
            alertDialog.cancel()
        }
    }
}