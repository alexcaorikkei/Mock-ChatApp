package com.example.baseproject.navigation

import android.os.Bundle
import android.util.Log
import com.example.baseproject.R
import com.example.core.navigationComponent.BaseNavigatorImpl
import com.example.setting.DemoNavigation
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AppNavigatorImpl @Inject constructor() : BaseNavigatorImpl(),
    AppNavigation, DemoNavigation {
    override fun openSplashToLoginScreen(bundle: Bundle?) {
        openScreen(R.id.action_splashFragment_to_loginFragment, bundle)
    }

    override fun openRegisterToLoginScreen(bundle: Bundle?) {
        navigateUp()
    }

    override fun openLoginToRegisterScreen(bundle: Bundle?) {
        openScreen(R.id.action_loginFragment_to_registerFragment, bundle)
    }

    override fun openLoginToHomeScreen(bundle: Bundle?) {
        openScreen(R.id.action_loginFragment_to_homeFragment, bundle)
    }

    override fun openHomeToEditProfileScreen(bundle: Bundle?) {
        openScreen(R.id.action_homeFragment_to_editProfileFragment, bundle)
    }

    override fun openHomeToLoginScreen(bundle: Bundle?) {
        openScreen(R.id.action_homeFragment_to_loginFragment, bundle)
    }

    override fun openHomeToChatScreen(bundle: Bundle?) {
        openScreen(R.id.action_homeFragment_to_chatFragment, bundle)
    }

    override fun openDemoViewPager(bundle: Bundle?) {

    }
}