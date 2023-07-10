package com.example.baseproject.ui.home.profile


import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentEditProfileBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, ProfileViewModel>(R.layout.fragment_edit_profile) {
    @Inject
    lateinit var appNavigaiton: AppNavigation
    private val viewModel: ProfileViewModel by activityViewModels()
    override fun getVM() = viewModel

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.profileResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Failure -> {}
                is Response.Loading -> {}
                is Response.Success -> {
                    binding.etName.setText(response.data.displayName)
                    binding.etPhoneNumber.setText(response.data.phoneNumber)
                    binding.tvBirthday.text = response.data.birthday
                }
            }
        }
    }
}