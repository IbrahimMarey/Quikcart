package com.example.quikcart.ui.profile

import androidx.lifecycle.ViewModel

class ProfileViewModel:ViewModel() {
     var navigator:Navigator?=null
    fun navigateToOrdersFragment(){
        navigator?.navigateToOrdersFragment()
    }
}