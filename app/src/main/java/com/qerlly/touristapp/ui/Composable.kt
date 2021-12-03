package com.qerlly.touristapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qerlly.touristapp.R
import com.qerlly.touristapp.ui.start.StartViewModel


@Composable
fun EmailTextField(viewModel: StartViewModel, emailTextState: MutableState<String>) =
    Column {
        val isInvalidEmail = viewModel.isInvalidEmail.collectAsState()

        val invalidEmailFormat = viewModel.invalidEmailFormat.collectAsState()

        OutlinedTextField(
            value = emailTextState.value,
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
            label = { Text(text = stringResource(R.string.e_mail)) },
            onValueChange = { newValue ->
                emailTextState.value = newValue
                viewModel.isInvalidEmail.value = false
                viewModel.invalidEmailFormat.value = false
            },
            isError = isInvalidEmail.value || invalidEmailFormat.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        if(invalidEmailFormat.value) ErrorText(stringResource(R.string.login_email_invalid))
        if(isInvalidEmail.value) ErrorText(stringResource(R.string.login_email_unknown))
    }

@Composable
fun PasswordTextField(viewModel: StartViewModel, passwordTextState: MutableState<String>) =
    Column {
        val isPasswordEmpty = viewModel.isPasswordEmpty.collectAsState()

        val isPasswordInvalid = viewModel.isPasswordInvalid.collectAsState()

        val isPasswordNotMatch = viewModel.isPasswordNotMatch.collectAsState()

        val passwordVisibility = remember { mutableStateOf(false) }

        OutlinedTextField(
            value = passwordTextState.value,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.password)) },
            onValueChange = { newValue ->
                passwordTextState.value = newValue
                viewModel.isPasswordInvalid.value = false
                viewModel.isPasswordEmpty.value = false
                viewModel.isPasswordNotMatch.value = false
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            isError = isPasswordInvalid.value || isPasswordEmpty.value || isPasswordNotMatch.value,
            trailingIcon = { TrailingIcon(passwordVisibility) }
        )

        if(isPasswordInvalid.value) ErrorText(stringResource(R.string.login_password_not_valid))
        if(isPasswordEmpty.value) ErrorText(stringResource(R.string.login_password_empty))
        if(isPasswordNotMatch.value) ErrorText(stringResource(R.string.passwords_do_not_match))
    }

@Composable
fun TrailingIcon(passwordVisibility: MutableState<Boolean>) =
    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
        if (passwordVisibility.value) {
            Icon(Icons.Filled.VisibilityOff, stringResource(R.string.login_password))
        } else {
            Icon(Icons.Filled.Visibility, stringResource(R.string.login_password))
        }
    }

@Composable
fun ErrorText(errorText: String) = Text(
    modifier = Modifier.padding(top = 4.dp, start = 16.dp),
    text = errorText,
    color = MaterialTheme.colors.error,
    fontSize = 12.sp
)