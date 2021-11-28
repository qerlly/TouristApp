package com.qerlly.touristapp.ui.startup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.qerlly.touristapp.R
import com.qerlly.touristapp.databinding.FragmentRegistrationBinding
import com.qerlly.touristapp.model.web.RegistrationDataModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var binding: FragmentRegistrationBinding? = null

    private val viewModel: StartupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            signUnButton.setOnClickListener {
                if(regEmail.text!!.isNotEmpty() && regLogin.text!!.isNotEmpty()
                    && regPassword.text!!.isNotEmpty() && regRepassword.text!!.isNotEmpty()) {
                    if(regPassword.text!!.toString() == regRepassword.text!!.toString()) {
                        viewModel.register(
                            RegistrationDataModel(
                                regEmail.text.toString(),
                                regLogin.text.toString(),
                                "", "", regPassword.text.toString(), regRepassword.text.toString()
                            )
                        )
                    }
                } else { handleDataException() }
            }
        }
    }

    private fun handleDataException() {
        Snackbar.make(binding!!.root, R.string.login_data_error, Snackbar.LENGTH_LONG).show()
    }

    private fun handleNetworkException() {
        Snackbar.make(binding!!.root, R.string.login_network_error, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}