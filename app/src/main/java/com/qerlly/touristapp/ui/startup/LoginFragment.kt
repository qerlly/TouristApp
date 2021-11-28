package com.qerlly.touristapp.ui.startup


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.form.FormResult
import com.google.android.material.snackbar.Snackbar
import com.qerlly.touristapp.R
import com.qerlly.touristapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import androidx.core.content.getSystemService
import androidx.navigation.fragment.NavHostFragment

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

            inputLayout(binding!!.loginEmailLayout, name = "email") {
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
            val email = result["email"]?.asString()
            val password = result["password"]?.asString()
            if (email != null && password != null) {
                onLogin(email, password)
            }
        }
    }

    private fun onLogin(email: String, password: String) {
        lifecycleScope.launch {
            try {
                binding!!.signInButton.isEnabled = false
                startupViewModel.onLoginAsync(email, password).await()
                (activity as StartupActivity).startMainActivity()
            } /*catch (e: FirebaseAuthInvalidCredentialsException) {
                handleInvalidCredentials()
            } catch (e: FirebaseAuthInvalidUserException) {
                handleInvalidEmail()
            } catch (e: FirebaseNetworkException) {
                handleNetworkException()
            }*/ catch (e: IllegalArgumentException) {
                //do nothing, handled by validation
            } catch (e: Exception) {
                Timber.e(e)
                handleUnknownError()
            } finally {
                binding!!.signInButton.isEnabled = true
                startupViewModel.loginHasBeenTried.value = true
                loginForm?.validate()
            }
        }
    }

    private fun handleInvalidCredentials() {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                startupViewModel.isPasswordInvalid.value = false
                binding!!.loginPassword.removeTextChangedListener(this)
            }
        }
        startupViewModel.isPasswordInvalid.value = true
        binding!!.loginPassword.setText("")
        binding!!.loginPassword.addTextChangedListener(listener)
    }

    private fun handleInvalidEmail() {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                startupViewModel.isInvalidEmail.value = false
                binding!!.loginEmail.removeTextChangedListener(this)
            }
        }
        startupViewModel.isInvalidEmail.value = true
        binding!!.loginEmail.addTextChangedListener(listener)
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