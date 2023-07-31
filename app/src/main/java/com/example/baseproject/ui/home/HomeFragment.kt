package com.example.baseproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentHomeBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.services.AppNotificationService
import com.example.baseproject.utils.Permission
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject



@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var navController: NavController

    @Inject
    lateinit var rxPreferences: RxPreferences
    override fun getVM() = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = (childFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment).navController
        binding.bottomNav.setupWithNavController(navController)
        Permission.askNotificationPermission(requireActivity(), requestPermissionLauncher)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            "FCM SDK (and your app) can post notifications.".toast(requireContext())
        } else {
            "FCM SDK (and your app) cannot post notifications.".toast(requireContext())
        }
    }
}