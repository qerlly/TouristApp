package com.qerlly.touristapp.ui.start.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.material.composethemeadapter.MdcTheme
import com.qerlly.touristapp.R
import com.qerlly.touristapp.ui.EmailTextField
import com.qerlly.touristapp.ui.PasswordTextField
import com.qerlly.touristapp.ui.start.Destinations
import com.qerlly.touristapp.ui.start.StartViewModel

@Composable
fun LoginScreen(navController: NavHostController, viewModel: StartViewModel, onClick: (String, String) -> Unit) = MdcTheme {

    val loginButtonEnabled = viewModel.loginButtonEnabled.collectAsState()

    val emailTextState = remember { mutableStateOf("") }

    val passwordTextState = remember { mutableStateOf("") }
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.welcome),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
            EmailTextField(viewModel, emailTextState)
            PasswordTextField(viewModel, passwordTextState)
            Button(
                onClick = {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTextState.value).matches())
                        viewModel.invalidEmailFormat.value = true
                    else if (passwordTextState.value.length < 6) viewModel.isPasswordEmpty.value =
                        true
                    else onClick(emailTextState.value, passwordTextState.value)
                },
                modifier = Modifier.fillMaxWidth().height(64.dp).padding(top = 20.dp),
                enabled = loginButtonEnabled.value
            ) {
                Text(text = stringResource(R.string.login).uppercase())
            }
            Button(
                onClick = { navController.navigate(Destinations.REGISTRATION_SCREEN) },
                modifier = Modifier.fillMaxWidth().height(64.dp).padding(top = 20.dp),
            ) {
                Text(text = stringResource(R.string.registration).uppercase())
            }
            Text(
                text = stringResource(R.string.forgot_password),
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                modifier = Modifier.padding(top = 26.dp).clickable(enabled = true, role = Role.Button)
                    { navController.navigate(Destinations.FORGOT_SCREEN) }
            )
        }
    }
}