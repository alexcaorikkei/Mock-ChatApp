package com.example.baseproject.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentRegisterBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.extension.makeLink
import com.example.baseproject.extension.validate
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.custom.MyPasswordTransformationMethod
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalArgumentException
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment() :
    BaseFragment<FragmentRegisterBinding, RegisterViewModel>(R.layout.fragment_register) {
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: RegisterViewModel by viewModels()
    override fun getVM() = viewModel

    @Inject
    lateinit var rxPreferences: RxPreferences

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.apply {
            etPassword.transformationMethod = MyPasswordTransformationMethod()
            tvPoliciesAndTerms.makeLink(
                Pair(getString(R.string.policies), View.OnClickListener {
                    val dialogBuilder = AlertDialog.Builder(requireContext())
                    dialogBuilder.setMessage(getString(R.string.policies))
                        .setCancelable(true)
                        .show()
                }),
                Pair(getString(R.string.terms), View.OnClickListener {
                    val dialogBuilder = AlertDialog.Builder(requireContext())
                    dialogBuilder.setMessage(getString(R.string.terms))
                        .setCancelable(true)
                        .show()
                })
            )
            tvTitleHadAccount.makeLink(
                Pair(getString(R.string.login), View.OnClickListener {
                    appNavigation.openRegisterToLoginScreen()
                })
            )
        }
    }

    override fun bindingAction() {
        super.bindingAction()
        viewModel.apply {
            signUpResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        resources.getString(R.string.sign_up_successfully).toast(requireContext())
                        appNavigation.openRegisterToLoginScreen()
                    }

                    is Response.Failure -> {
                        when (response.e) {
                            is FirebaseAuthUserCollisionException -> {
                                resources.getString(R.string.email_already_exists)
                                    .toast(requireContext())
                            }

                            is IllegalArgumentException -> {
                                resources.getString(R.string.email_or_password_is_empty)
                                    .toast(requireContext())
                            }

                            is FirebaseNetworkException -> {
                                resources.getString(R.string.no_internet_connection)
                                    .toast(requireContext())
                            }

                            else -> {
                                response.e.toString().toast(requireContext())
                            }
                        }
                    }
                }
            }
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.apply {
            signUpResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                        binding.btnRegister.isEnabled = false
                        binding.etEmail.isEnabled = false
                        binding.etPassword.isEnabled = false
                        binding.etName.isEnabled = false
                        binding.includeProgress.visibility = View.VISIBLE
                    }

                    is Response.Success -> {
                        binding.btnRegister.isEnabled = true
                        binding.etEmail.isEnabled = true
                        binding.etPassword.isEnabled = true
                        binding.etName.isEnabled = true
                        binding.includeProgress.visibility = View.GONE
                    }

                    is Response.Failure -> {
                        binding.btnRegister.isEnabled = true
                        binding.etEmail.isEnabled = true
                        binding.etPassword.isEnabled = true
                        binding.etName.isEnabled = true
                        binding.includeProgress.visibility = View.GONE
                    }
                }
            }
            validator.observe(viewLifecycleOwner) { validator ->
                binding.btnRegister.isEnabled = validator
            }
        }

        binding.apply {
            etEmail.validate { email ->
                if (email.isEmpty()) {
                    binding.etEmail.error = getString(R.string.email_is_empty)
                    viewModel.setValidState(isValidEmail = false)
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etEmail.error = getString(R.string.email_is_invalid)
                    viewModel.setValidState(isValidEmail = false)
                } else {
                    binding.etEmail.error = null
                    viewModel.setValidState(isValidEmail = true)
                }
            }
            etName.validate { name ->
                if (name.isEmpty()) {
                    binding.etName.error = getString(R.string.name_is_empty)
                    viewModel.setValidState(isValidDisplayName = false)
                } else {
                    binding.etName.error = null
                    viewModel.setValidState(isValidDisplayName = true)
                }
            }
            etPassword.validate { password ->
                if (password.isEmpty()) {
                    binding.etPassword.error = getString(R.string.password_is_empty)
                    viewModel.setValidState(isValidPassword = false)
                } else if (password.length < 6) {
                    binding.etPassword.error = getString(R.string.password_is_to_weak)
                    viewModel.setValidState(isValidPassword = false)
                } else {
                    binding.etPassword.error = null
                    viewModel.setValidState(isValidPassword = true)
                }
            }
            cbPoliciesAndTerms.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setValidState(isChecked = isChecked)
            }
        }
    }

    override fun setOnClick() {
        binding.apply {
            btnRegister.setOnClickListener {
                viewModel.signUp(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.etName.text.toString()
                )
                lifecycleScope.launch {
                    rxPreferences.setEmail(binding.etEmail.text.toString())
                }
            }

            btnBack.setOnClickListener {
                appNavigation.openRegisterToLoginScreen()
            }
        }
    }
}