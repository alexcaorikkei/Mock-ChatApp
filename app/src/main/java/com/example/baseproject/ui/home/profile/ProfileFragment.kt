package com.example.baseproject.ui.home.profile

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentProfileBinding
import com.example.baseproject.domain.model.MessageModel
import com.example.baseproject.domain.model.NotiModel
import com.example.baseproject.domain.model.Notification
import com.example.baseproject.domain.model.Response
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.services.AppNotificationService
import com.example.baseproject.utils.Language
import com.example.baseproject.utils.MessageRetrofitUtil
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.setLanguage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {
    @Inject
    lateinit var appNavigation: AppNavigation
    @Inject
    lateinit var rxPreferences: RxPreferences
    private val viewModel: ProfileViewModel by viewModels()
    override fun getVM() = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        lifecycleScope.launch {
            binding.tvLanguage.text = getString(
                Language
                    .getLanguageByCode(rxPreferences.getLanguage().first())
                    .displayId
            )
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.profileResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Failure -> {}
                Response.Loading -> {}
                is Response.Success -> {
                    binding.tvName.text = response.data.displayName
                    binding.tvEmail.text = response.data.email
                    if(response.data.profilePicture.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(response.data.profilePicture.toUri())
                            .into(binding.ivProfile)
                            .onLoadStarted(
                                AppCompatResources.getDrawable(
                                    binding.ivProfile.context,
                                    com.example.core.R.drawable.ic_avatar_default
                                )
                            )
                    }
                }
            }
        }
    }

    override fun bindingAction() {
        super.bindingAction()
        viewModel.logOutResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Failure -> {}
                Response.Loading -> {}
                is Response.Success -> {
                    appNavigation.openHomeToLoginScreen()
                }
            }
        }
    }
    override fun setOnClick() {
        super.setOnClick()
        binding.apply {
            btnEditProfile.setOnClickListener() {
                appNavigation.openHomeToEditProfileScreen()
            }
            llLogout.setOnClickListener() {
//                activity?.stopService(Intent(activity, AppNotificationService::class.java))
                viewModel.logOut()
            }
            llLanguages.setOnClickListener {
                val popupMenu = android.widget.PopupMenu(requireContext(), it, android.view.Gravity.END)
                popupMenu.menuInflater.inflate(R.menu.language_drop, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    lifecycleScope.launch {
                        val currentLanguage = rxPreferences.getLanguage().first()
                        val newLanguage = Language.getLanguageByItemId(item.itemId).code
                        if (currentLanguage == newLanguage) {
                            return@launch
                        }
                        changeLanguage(Language.getLanguageByItemId(item.itemId).code)
                        binding.tvLanguage.text = item.title
                        appNavigation.openHomeToLoginScreen()
                    }
                    true
                }
                popupMenu.show()
            }

            llNotifications.setOnClickListener {
                val intent = Intent()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                } else {
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS")
                    intent.putExtra("app_package", requireContext().packageName)
                    intent.putExtra("app_uid", requireContext().applicationInfo.uid)
                }
                requireContext().startActivity(intent)
            }
        }
    }

    private fun changeLanguage(language: String) {
        requireContext().setLanguage(language)
        lifecycleScope.launch {
            rxPreferences.setLanguage(language)
        }
    }
}