package com.example.baseproject.ui.home.profile

import android.os.Bundle
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentProfileBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.setLanguage
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {
    @Inject
    lateinit var appNavigation: AppNavigation
    @Inject
    lateinit var rxPreferences: RxPreferences
    private val viewModel: ProfileViewModel by activityViewModels()
    override fun getVM() = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        if(viewModel.profileResponse.value == null) {
            viewModel.getProfile()
        }
        lifecycleScope.launch {
            when(rxPreferences.getLanguage().first()) {
                "en" -> {
                    binding.tvLanguage.text = getString(R.string.english)
                }
                "vi" -> {
                    binding.tvLanguage.text = getString(R.string.vietnamese)
                }
            }
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
                            .into(binding.ivProfile);
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
                viewModel.logOut()
            }
            llLanguages.setOnClickListener {
                val popupMenu = android.widget.PopupMenu(requireContext(), it, android.view.Gravity.END)
                popupMenu.menuInflater.inflate(R.menu.language_drop, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    when(item.itemId) {
                        R.id.en -> {
                            lifecycleScope.launch {
                                changeLanguage("en")
                            }
                            tvLanguage.text = getString(R.string.english)
                        }
                        R.id.vi -> {
                            lifecycleScope.launch {
                                changeLanguage("vi")
                                tvLanguage.text = getString(R.string.vietnamese)
                            }
                        }
                    }
                    getString(R.string.change_language_toast).toast(requireContext())
                    true
                }
                popupMenu.show()
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