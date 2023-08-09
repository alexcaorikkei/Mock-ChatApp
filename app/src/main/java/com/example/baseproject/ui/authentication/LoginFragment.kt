package com.example.baseproject.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentLoginBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.extension.makeLink
import com.example.baseproject.extension.validate
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.custom.MyPasswordTransformationMethod
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {

    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: LoginViewModel by viewModels()
    override fun getVM() = viewModel
    @Inject
    lateinit var rxPreferences: RxPreferences

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.apply {
            tvTitleNoAccount.makeLink(
                Pair(getString(R.string.registry), View.OnClickListener {
                    appNavigation.openLoginToRegisterScreen()
                })
            )

            etPassword.transformationMethod = MyPasswordTransformationMethod()
        }
        lifecycleScope.launch {
            val email = rxPreferences.getEmail()
            if(email.first() != null) {
                binding.etEmail.setText(email.first().toString())
                viewModel.setValidState(isValidEmail = true)
            }
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Timber.d("token: $token")
        })
    }

    override fun bindingStateView() {
        super.bindingStateView()
        binding.apply {
            etEmail.validate { email ->
                if(email.isEmpty()) {
                    binding.etEmail.error = getString(R.string.email_is_empty)
                    viewModel.setValidState(isValidEmail = false)
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etEmail.error = getString(R.string.email_is_invalid)
                    viewModel.setValidState(isValidEmail = false)
                }
                else {
                    binding.etEmail.error = null
                    viewModel.setValidState(isValidEmail = true)
                }
            }
            etPassword.validate { password ->
                if(password.isEmpty()) {
                    binding.etPassword.error = getString(R.string.password_is_empty)
                    viewModel.setValidState(isValidPassword = false)
                } else {
                    binding.etPassword.error = null
                    viewModel.setValidState(isValidPassword = true)
                }
            }
        }
        viewModel.apply {
            validator.observe(viewLifecycleOwner) { validator ->
                binding.btnLogin.isEnabled = validator
            }
        }
    }

    override fun bindingAction() {
        super.bindingAction()
        viewModel.apply {
            signInResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                        binding.btnLogin.isEnabled = false
                        binding.etEmail.isEnabled = false
                        binding.etPassword.isEnabled = false
                        binding.includeProgress.visibility = View.VISIBLE
                    }
                    is Response.Success -> {
                        binding.btnLogin.isEnabled = true
                        binding.etEmail.isEnabled = true
                        binding.etPassword.isEnabled = true
                        binding.includeProgress.visibility = View.GONE
                        appNavigation.openLoginToHomeScreen()
                        startService()
                    }
                    is Response.Failure -> {
                        binding.btnLogin.isEnabled = true
                        binding.etEmail.isEnabled = true
                        binding.etPassword.isEnabled = true
                        binding.includeProgress.visibility = View.GONE
                        when(response.e) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                resources.getString(R.string.password_is_incorrect).toast(requireContext())
                            }
                            is IllegalArgumentException -> {
                                resources.getString(R.string.email_or_password_is_empty).toast(requireContext())
                            }
                            is FirebaseNetworkException -> {
                                resources.getString(R.string.no_internet_connection).toast(requireContext())
                            }
                            is FirebaseAuthInvalidUserException -> {
                                resources.getString(R.string.email_is_not_registered).toast(requireContext())
                            }
                            else -> {
                                response.e.toString().toast(requireContext())
                            }
                        }
                    }
                }
            }
        }
        if(viewModel.isLogin) {
            appNavigation.openLoginToHomeScreen()
            startService()
        }
    }

    override fun setOnClick() {
        binding.apply {
            btnLogin.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.signIn(
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        rxPreferences.getNotificationToken().first().toString()
                    )
                    rxPreferences.setEmail(binding.etEmail.text.toString())
                }
            }
        }
    }

    private fun startService() {
//        val intent = Intent(requireContext(), AppNotificationService::class.java)
//        activity?.startService(intent)
//
//        val intent2 = Intent(requireContext(), FirebaseNotificationService::class.java)
//        activity?.startService(intent2)
    }
}
