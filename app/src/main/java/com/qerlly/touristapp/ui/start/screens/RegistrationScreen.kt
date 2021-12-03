package com.qerlly.touristapp.ui.start.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.material.composethemeadapter.MdcTheme
import com.qerlly.touristapp.R
import com.qerlly.touristapp.ui.EmailTextField
import com.qerlly.touristapp.ui.PasswordTextField
import com.qerlly.touristapp.ui.start.StartViewModel

@Composable
fun RegistrationScreen(navController: NavHostController, viewModel: StartViewModel, onClick: (String, String) -> Unit) = MdcTheme {

    val regButtonEnabled = viewModel.registrationButtonEnabled.collectAsState()

    val emailTextState = remember { mutableStateOf("") }

    val passwordTextState = remember { mutableStateOf("") }

    val rePasswordTextState = remember { mutableStateOf("") }

    BackHandler { viewModel.clearFlags() }

    Card(shape = RoundedCornerShape(4.dp), elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.registration),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
            EmailTextField(viewModel, emailTextState)
            PasswordTextField(viewModel, passwordTextState)
            PasswordTextField(viewModel, rePasswordTextState)
            Button(
                onClick = {
                    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTextState.value).matches())
                        viewModel.invalidEmailFormat.value = true
                    else if(passwordTextState.value.length < 6) viewModel.isPasswordEmpty.value = true
                    else if (rePasswordTextState.value != passwordTextState.value) viewModel.isPasswordNotMatch.value = true
                    else onClick(emailTextState.value, passwordTextState.value)
                },
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                enabled = regButtonEnabled.value
            ) {
                Text(text = stringResource(R.string.registration).uppercase())
            }
            Button(
                onClick = { viewModel.clearFlags().also { navController.popBackStack() } },
                modifier = Modifier.fillMaxWidth().height(64.dp).padding(top = 20.dp),
                enabled = regButtonEnabled.value
            ) {
                Text(text = stringResource(R.string.cancel).uppercase())
            }
        }
    }
}