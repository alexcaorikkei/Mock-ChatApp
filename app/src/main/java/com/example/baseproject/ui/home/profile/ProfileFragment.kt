package com.example.baseproject.ui.home.profile

import androidx.fragment.app.viewModels
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
    private val viewModel: ProfileViewModel by viewModels()
    override fun getVM() = viewModel
    override fun bindingAction() {
        super.bindingAction()
        viewModel.logOutResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Failure -> {

                }
                Response.Loading -> {

                }
                is Response.Success -> {
                    appNavigation.openHomeToLoginScreen()
                }
            }
        }
    }
    override fun setOnClick() {
        super.setOnClick()
        binding.btnEditProfile.setOnClickListener() {
            appNavigation.openHomeToEditProfileScreen()
        }
        binding.llLogout.setOnClickListener() {
            viewModel.logOut()
        }
    }
}