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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.qerlly.touristapp.R
import com.qerlly.touristapp.ui.EmailTextField
import com.qerlly.touristapp.ui.start.StartViewModel

@Composable
fun ForgotPasswordScreen(controller: NavHostController, viewModel: StartViewModel, onClick: (String) -> Unit) {

    val emailTextState = remember { mutableStateOf("") }

    BackHandler { viewModel.clearFlags() }

    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.forgot_password),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
            EmailTextField(viewModel, emailTextState)
            Button(
                onClick = {
                    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTextState.value).matches())
                        viewModel.invalidEmailFormat.value = true
                    else
                        onClick(emailTextState.value).also {
                            viewModel.clearFlags()
                            controller.popBackStack()
                        }
                },
                modifier = Modifier.fillMaxWidth().height(64.dp).padding(top = 20.dp),
            ) {
                Text(text = stringResource(R.string.send).uppercase())
            }
        }
    }
}