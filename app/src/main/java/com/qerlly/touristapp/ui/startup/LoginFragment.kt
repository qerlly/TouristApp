package com.qerlly.touristapp.ui.startup


import android.accounts.NetworkErrorException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.form.FormResult
import com.google.android.material.snackbar.Snackbar
import com.qerlly.touristapp.R
import com.qerlly.touristapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null

    private val startupViewModel: StartupViewModel by viewModels()

    private var loginForm: Form? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        inflateUI(inflater, container)
        return binding?.root
    }

    private fun inflateUI(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        initNavController()

        loginForm = form {
            useRealTimeValidation(disableSubmit = true)

            inputLayout(binding!!.loginEmailLayout, name = "login") {
                conditional(startupViewModel.loginHasBeenTried::value) {
                    isEmail().apply {
                        description(R.string.login_email_invalid)
                    }
                }
                assert(resources.getString(R.string.login_email_unknown)) {
                    !startupViewModel.isInvalidEmail.value
                }
            }
            inputLayout(binding!!.loginPasswordLayout, "password") {
                assert(resources.getString(R.string.login_password_not_valid)) {
                    !startupViewModel.isPasswordInvalid.value
                }
                conditional(startupViewModel.loginHasBeenTried::value) {
                    isNotEmpty().apply {
                        description(R.string.login_password_empty)
                    }
                }
                assert(resources.getString(R.string.login_password_empty)) {
                    !startupViewModel.isPasswordEmpty.value
                }
            }
            submitWith(binding!!.signInButton, this@LoginFragment::handleFormResult)
        }

        binding!!.loginPassword.setOnEditorActionListener { v, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    loginForm?.validate()?.let {
                        handleFormResult(it)
                        activity?.getSystemService<InputMethodManager>()!!
                            .hideSoftInputFromWindow(v.windowToken, 0)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun initNavController() {
        val navController = (activity?.supportFragmentManager
            ?.findFragmentById(R.id.startup_fragment_host) as NavHostFragment)
            .navController

        binding?.signUpButton?.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding?.forgotPassword?.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    private fun handleFormResult(result: FormResult) {
        lifecycleScope.launch {
            val login = result["login"]?.asString()
            val password = result["password"]?.asString()
            if (login != null && password != null) {
                onLogin(login, password)
            }
        }
    }

    private fun onLogin(login: String, password: String) {
        lifecycleScope.launch {
            try {
                binding!!.signInButton.isEnabled = false
                startupViewModel.login()
                //(activity as StartupActivity).startMainActivity()
            } catch (e: IllegalArgumentException) {
                Timber.e(e)
                handleDataException()
            } catch (e: NetworkErrorException) {
                Timber.e(e)
                handleNetworkException()
            } catch (e: Exception) {
                Timber.e(e)
                handleUnknownError()
            } finally {
                binding!!.signInButton.isEnabled = true
                loginForm?.validate()
            }
        }
    }

    private fun handleDataException() {
        Snackbar.make(binding!!.root, R.string.login_data_error, Snackbar.LENGTH_LONG).show()
    }

    private fun handleNetworkException() {
        Snackbar.make(binding!!.root, R.string.login_network_error, Snackbar.LENGTH_LONG).show()
    }

    private fun handleUnknownError() {
        Snackbar.make(binding!!.root, R.string.login_unknown_error, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}