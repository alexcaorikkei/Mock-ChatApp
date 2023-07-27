package com.example.baseproject.ui.home.profile


import android.net.Uri
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentEditProfileBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.extension.transformIntoDatePicker
import com.example.baseproject.extension.validate
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.domain.model.UserModel
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, ProfileViewModel>(R.layout.fragment_edit_profile), BottomSheetListener {
    @Inject
    lateinit var appNavigaiton: AppNavigation
    private val viewModel: ProfileViewModel by viewModels()
    private var imageProfileUri : Uri? = null
    override fun getVM() = viewModel

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.validator.observe(viewLifecycleOwner) { isValid ->
            binding.tvSave.isEnabled = isValid
        }
        viewModel.profileResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Failure -> {
                    binding.includeProgress.visibility = View.GONE
                    setComponentState(true)
                }
                is Response.Loading -> {
                    binding.includeProgress.visibility = View.VISIBLE
                    setComponentState(false)
                }
                is Response.Success -> {
                    binding.apply {
                        includeProgress.visibility = View.GONE
                        etName.setText(response.data.displayName)
                        etPhoneNumber.setText(response.data.phoneNumber)
                        etBirthday.setText(response.data.birthday)
                        if(response.data.profilePicture.isNotEmpty()) {
                            Glide.with(requireContext())
                                .load(response.data.profilePicture.toUri())
                                .into(ivProfile);
                        }
                    }
                    setComponentState(true)
                }
            }
        }
        viewModel.editResponse.observe(viewLifecycleOwner) {response ->
            when(response) {
                is Response.Failure -> {}
                Response.Loading -> {}
                is Response.Success -> {
                    appNavigaiton.openEditProfileToProfileScreen()
                }
            }
        }
        binding.apply {
            etName.validate { name ->
                if(name.isEmpty()) {
                    etName.error = getString(R.string.name_is_empty)
                    viewModel.setValidState(isValidName = false)
                } else {
                    etName.error = null
                    viewModel.setValidState(isValidName = true)
                }
            }
            etPhoneNumber.validate { phoneNumber ->
                if(phoneNumber.startsWith("0") && phoneNumber.length == 10 || phoneNumber.isEmpty()) {
                    etPhoneNumber.error = null
                    viewModel.setValidState(isValidPhone = true)
                } else {
                    etPhoneNumber.error = getString(R.string.phone_number_is_invalid)
                    viewModel.setValidState(isValidPhone = false)
                }
            }
        }
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.apply {
            tvSave.setOnClickListener() {
                viewModel.updateProfile(
                    UserModel(
                        displayName = etName.text.toString(),
                        phoneNumber = etPhoneNumber.text.toString(),
                        birthday = etBirthday.text.toString(),
                    ),
                    imageProfileUri
                )
            }
            etBirthday.transformIntoDatePicker(requireContext(), "dd/MM/yyyy")
            btnBack.setOnClickListener {
                appNavigaiton.openEditProfileToProfileScreen()
            }
            btnChangeAvatar.setOnClickListener {
                val bottomSheetFragment = BottomSheetFragment(this@EditProfileFragment)
                bottomSheetFragment.show(childFragmentManager, "BottomSheetFragment")
            }
        }
    }

    private fun setComponentState(enabled: Boolean) {
        binding.apply {
            etName.isEnabled = enabled
            etPhoneNumber.isEnabled = enabled
            etBirthday.isEnabled = enabled
            tvSave.isClickable = enabled
        }
    }

    override fun onGetImageSuccess(uri: Uri) {
        imageProfileUri = uri
        binding.ivProfile.setImageURI(imageProfileUri)
    }
}