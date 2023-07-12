package com.example.baseproject.ui.home.profile

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentProfileBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: ProfileViewModel by activityViewModels()
    override fun getVM() = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        if(viewModel.profileResponse.value == null) {
            viewModel.getProfile()
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
        }
    }
}