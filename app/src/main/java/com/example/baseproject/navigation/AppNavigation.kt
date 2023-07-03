package com.example.baseproject.navigation

import android.os.Bundle
import com.example.core.navigationComponent.BaseNavigator

interface AppNavigation : BaseNavigator {
    fun openSplashToLoginScreen(bundle: Bundle? = null)
    fun openRegisterToLoginScreen(bundle: Bundle? = null)
    fun openLoginToRegisterScreen(bundle: Bundle? = null)
    fun openLoginToHomeScreen(bundle: Bundle? = null)
    fun openHomeToEditProfileScreen(bundle: Bundle? = null)
    fun openHomeToLoginScreen(bundle: Bundle? = null)
}