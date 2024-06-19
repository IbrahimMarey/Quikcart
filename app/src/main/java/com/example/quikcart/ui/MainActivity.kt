package com.example.quikcart.ui

import android.content.DialogInterface
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.quikcart.R
import com.example.quikcart.databinding.ActivityMainBinding
import com.example.quikcart.utils.AlertUtil
import com.example.quikcart.utils.NetworkChangeReceiver
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    private val HOME_ITEM = R.id.homeFragment
    private val FAVORITE_ITEM = R.id.favoriteFragment
    private val CART_ITEM = R.id.cartFragment
    private val PROFILE_ITEM = R.id.profileFragment
    private val SEARCH_ITEM = R.id.searchFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            initNavHost()
            setUpBottomNavigation()
        }
        networkChangeReceiver=NetworkChangeReceiver{isConnected->
            handleNetworkChange(isConnected)
        }

    }

    override fun onResume() {
        super.onResume()
        Log.e("TAG", "onResume: ", )
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkChangeReceiver)
    }

    private fun handleNetworkChange(connected: Boolean) {
        if(!connected){
            AlertUtil.showCustomAlertDialog(this,
                title = "No Internet Connection",
                "Please check your internet connection and try again.",
                positiveText = "Exit"
            ) { dialog, _ -> dialog.dismiss();finish() }
        }else {
            AlertUtil.dismissAlertDialog()
        }
    }

    private fun ActivityMainBinding.setUpBottomNavigation() {
        val bottomNavigationItems = mutableListOf(
            CurvedBottomNavigation.Model(HOME_ITEM, getString(R.string.home), R.drawable.ic_home),
            CurvedBottomNavigation.Model(SEARCH_ITEM, getString(R.string.search), R.drawable.ic_search),
            CurvedBottomNavigation.Model(FAVORITE_ITEM, getString(R.string.wish_list), R.drawable.ic_favorite),
            CurvedBottomNavigation.Model(CART_ITEM, getString(R.string.cart), R.drawable.ic_cart),
            CurvedBottomNavigation.Model(PROFILE_ITEM, getString(R.string.profile), R.drawable.ic_person),
            )
        bottomNavigation.apply {
            bottomNavigationItems.forEach { add(it) }
            setOnClickMenuListener {
                navController.navigate(it.id)
            }
            show(HOME_ITEM)
            setupNavController(navController)
        }
    }
    private fun initNavHost() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener{ controller , destination , argument ->
            if (destination.id  == R.id.paymentFragment
                || destination.id  == R.id.addressesFragment
                || destination.id  == R.id.mapFragment
                || destination.id  == R.id.ordersFragment
                || destination.id  == R.id.confirmOrderFirstScreenFragment
                || destination.id  == R.id.placeOrderFragment
                || destination.id  == R.id.productFragment
                )
            {
                binding.bottomNavigation.visibility = View.GONE
                if (destination.id  == R.id.mapFragment)
                {
                    binding.appToolBar.visibility = View.GONE
                }else
                {
                    binding.appToolBar.visibility = View.VISIBLE

                }

            }
            else if (destination.id == R.id.productDetailsFragment)
            {
                binding.bottomNavigation.visibility = View.GONE
                binding.appToolBar.visibility = View.GONE
            }
            else if (destination.id == R.id.reviewFragment)
            {
                binding.bottomNavigation.visibility = View.GONE
                binding.appToolBar.visibility = View.GONE
            }
            else{
                binding.appToolBar.visibility = View.VISIBLE
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

    }
}