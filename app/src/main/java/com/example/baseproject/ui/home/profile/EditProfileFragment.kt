package com.example.baseproject.ui.home.profile


import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.adapters.DatePickerBindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentEditProfileBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.extension.transformIntoDatePicker
import com.example.baseproject.extension.validate
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.profile.model.UserModel
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Timer
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
                    binding.etBirthday.setText(response.data.birthday)
                }
            }
        }
        binding.apply {
            etName.validate { name ->
                if(name.isEmpty()) {
                    etName.error = getString(R.string.name_is_empty)
                } else {
                    etName.error = null
                }
            }
            etPhoneNumber.validate { phoneNumber ->
                if(phoneNumber.isEmpty()) {
                    etPhoneNumber.error = getString(R.string.phone_number_is_empty)
                } else {
                    etPhoneNumber.error = null
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
                    )
                )
                appNavigaiton.openEditProfileToProfileScreen()
            }
            etBirthday.transformIntoDatePicker(requireContext(), "dd/MM/yyyy")
            btnBack.setOnClickListener {
                appNavigaiton.openEditProfileToProfileScreen()
            }
        }
    }
}