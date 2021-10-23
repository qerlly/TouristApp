package com.qerlly.touristapp.application.startup


import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.afollestad.vvalidator.form.Form
import com.qerlly.touristapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val startupViewModel: StartupViewModel by viewModels()

    private var loginForm: Form? = null


}