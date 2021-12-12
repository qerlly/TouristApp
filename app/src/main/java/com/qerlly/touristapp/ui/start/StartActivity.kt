package com.qerlly.touristapp.ui.start

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.qerlly.touristapp.R
import com.qerlly.touristapp.ui.main.MainActivity
import com.qerlly.touristapp.viewModels.StartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    private val startViewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            val state = startViewModel.isUserLogin.first { it != StartViewModel.Status.LOADING }
            if (state == StartViewModel.Status.NOT_LOGIN) {
                setContent { StartScreen() }
            } else {
                startMainActivity()
            }
        }
    }

    @Composable
    fun StartScreen() = MdcTheme {
        val navController = rememberNavController()
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Box(Modifier.padding(16.dp).height(260.dp)) {
                    Image(
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                        painter = painterResource(R.drawable.logo_img),
                        contentDescription = stringResource(R.string.app_name),
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 16.dp),
                        text = stringResource(R.string.app_name),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = Color.White
                    )
            }
            StartNavGraph(
                navController,
                startViewModel,
                this@StartActivity::onLogin,
                this@StartActivity::onRegister,
                this@StartActivity::onForgotPassword,
            )
        }
    }

    private fun onLogin(email: String, password: String) {
        lifecycleScope.launch {
            try {
                startViewModel.loginButtonEnabled.value = false
                startViewModel.login(email, password).await()
                startMainActivity()
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                startViewModel.isPasswordInvalid.value = true
            } catch (e: FirebaseAuthInvalidUserException) {
                startViewModel.isInvalidEmail.value = true
            } catch (e: FirebaseNetworkException) {
                networkError()
            } catch (e: IllegalArgumentException) {
                Timber.i("error validation")
            } catch (e: Exception) {
                Timber.e(e)
                unknownError()
            } finally {
                startViewModel.loginButtonEnabled.value = true
            }
        }
    }

    private fun onRegister(email: String, password: String) {
        lifecycleScope.launch {
            try {
                startViewModel.registrationButtonEnabled.value = false
                startViewModel.register(email, password).await()
                startMainActivity()
            } catch (e: FirebaseNetworkException) {
                networkError()
            } catch (e: FirebaseAuthUserCollisionException) {
                collisionError()
            } catch (e: IllegalArgumentException) {
                Timber.i("error validation")
            } catch (e: Exception) {
                Timber.e(e)
                unknownError()
            } finally {
                startViewModel.registrationButtonEnabled.value = true
            }
        }
    }

    private fun onForgotPassword(email: String) {
        try {
            startViewModel.forgetPassword(email)
            sendSuccess()
        } catch (e: Exception) {
            Timber.e(e)
            sendError()
        }
    }

    private fun sendSuccess() = Toast.makeText(applicationContext, R.string.send_success, Toast.LENGTH_LONG).show()

    private fun sendError() = Toast.makeText(applicationContext, R.string.send_error, Toast.LENGTH_LONG).show()

    private fun networkError() = Toast.makeText(applicationContext, R.string.login_network_error, Toast.LENGTH_LONG).show()

    private fun unknownError() = Toast.makeText(applicationContext, R.string.login_unknown_error, Toast.LENGTH_LONG).show()

    private fun collisionError() = Toast.makeText(applicationContext, R.string.collision, Toast.LENGTH_LONG).show()

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}