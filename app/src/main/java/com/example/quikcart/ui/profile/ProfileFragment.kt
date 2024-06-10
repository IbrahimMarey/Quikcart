package com.example.quikcart.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.quikcart.R
import com.example.quikcart.databinding.AboutUsDialogBinding
import com.example.quikcart.databinding.FragmentProfileBinding
import com.example.quikcart.models.network.CurrencyHelper
import com.example.quikcart.models.remote.CurrencySource
import com.example.quikcart.models.repos.CurrencyRepo
import com.example.quikcart.utils.PreferencesUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(),Navigator {

    private lateinit var binding:FragmentProfileBinding
    private lateinit var materialAboutUsBuilder: MaterialAlertDialogBuilder
    private lateinit var aboutUsDialog:AboutUsDialogBinding
    private lateinit var viewModel: ProfileViewModel
    private val preferencesUtils by lazy {
        PreferencesUtils.getInstance(requireActivity().application)
    }
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

        initViewModel()
        viewModel.navigator=this
        binding.profileViewModel=viewModel
        binding.addressSittings.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToAddressesFragment()
            Navigation.findNavController(it).navigate(action)//R.id.addressesFragment
        }
        binding.contactUs.setOnClickListener{
            contactUsDialog()
        }
        binding.aboutUs.setOnClickListener{
            aboutUsDialogOpening()
        }

        binding.currency.setOnClickListener {
            currencyDialog()
        }
    }

    private fun initViewModel() {
        val factory = ProfileModelFactory(CurrencyRepo.getInstance(CurrencySource(CurrencyHelper.currencyService)))
        viewModel = ViewModelProvider(this,factory)[ProfileViewModel::class.java]
//        viewModel=ViewModelProvider(this)[ProfileViewModel::class.java]
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

    private fun currencyDialog()
    {
        val currency = layoutInflater.inflate(R.layout.currency_dialog, null)

        val alertDialog = materialAboutUsBuilder.setView(currency)
            .setBackground(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.dialogue_background, requireActivity().theme
                )
            ).setCancelable(true).show()

        when(preferencesUtils.getCurrencyType())
        {
            PreferencesUtils.CURRENCY_USD ->currency.findViewById<RadioButton>(R.id.radioUSD).toggle()
            PreferencesUtils.CURRENCY_EGP ->currency.findViewById<RadioButton>(R.id.radioEGP).toggle()
        }
        currency.findViewById<RadioGroup>(R.id.currencyGroup).setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioUSD -> {
                    preferencesUtils.setCurrencyType(PreferencesUtils.CURRENCY_USD)
                    preferencesUtils.setUSDRate(viewModel.getCurrency().toFloat())
                    Log.i("TAG", "currencyDialog: ${viewModel.getCurrency().toFloat()}")
                }
                R.id.radioEGP -> preferencesUtils.setCurrencyType(PreferencesUtils.CURRENCY_EGP)
            }

        }
        currency.findViewById<Button>(R.id.button_save_currency).setOnClickListener {
            alertDialog.cancel()
        }
    }

    override fun navigateToOrdersFragment() {
        findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
    }
}