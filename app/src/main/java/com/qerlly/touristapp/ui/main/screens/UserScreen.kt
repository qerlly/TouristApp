package com.qerlly.touristapp.ui.main.screens

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qerlly.touristapp.R
import com.qerlly.touristapp.getActivity
import com.qerlly.touristapp.ui.ErrorText
import com.qerlly.touristapp.ui.TrailingIcon
import com.qerlly.touristapp.ui.start.StartActivity
import com.qerlly.touristapp.viewModels.UserSettingsViewModel
import kotlinx.coroutines.runBlocking
import java.util.regex.Pattern

@Composable
fun UserScreen() = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {

    val viewModel = hiltViewModel<UserSettingsViewModel>()

    val state = viewModel.dataState.collectAsState()

    UserCardButton(viewModel.activeUserEmail, viewModel.isUserActive, stringResource(R.string.logout), viewModel::logout)
    PasswordCardButton(viewModel, stringResource(R.string.change), viewModel::changePassword)
    UserCardSwitch(stringResource(R.string.loc_question), viewModel)
    UserCardEdit(viewModel)

    Button(
        enabled = state.value.tour.isNotEmpty(),
        onClick = { runBlocking { viewModel.leaveTour() } }
    ) {
        Text(text = stringResource(R.string.exit_tour).uppercase(),  style = MaterialTheme.typography.body1)
    }
}

@Composable
fun UserCardButton(text: String?, enabled: Boolean, buttonText: String, onClick: () -> Unit) = Card(
    shape = RoundedCornerShape(4.dp),
    elevation = 4.dp,
    modifier = Modifier.fillMaxWidth()
) {
    val activity = LocalContext.current.getActivity()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text ?: "", Modifier.fillMaxWidth(0.7f))
        Button(
            onClick = { onClick().also {
                activity?.startActivity(Intent(activity, StartActivity::class.java))
                activity?.finish()
            } },
            modifier = Modifier.height(32.dp),
            enabled = enabled
        ) {
            Text(text = buttonText.uppercase(), fontSize = 10.sp)
        }
    }
}

@Composable
fun UserCardSwitch(text: String, viewModel: UserSettingsViewModel) = Card(
    shape = RoundedCornerShape(4.dp),
    elevation = 4.dp,
    modifier = Modifier.fillMaxWidth()
) {
    val state = viewModel.localizationState.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, Modifier.fillMaxWidth(0.7f))
        Switch(
            checked = state.value,
            onCheckedChange = { viewModel.saveLocalizationState(it) },
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UserCardEdit(viewModel: UserSettingsViewModel) = Card(shape = RoundedCornerShape(4.dp), elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {

    val nameTextState = remember { mutableStateOf("") }

    val phoneTextState = remember { mutableStateOf("") }

    val nameErrorState = remember { mutableStateOf(false) }

    val phoneErrorState = remember { mutableStateOf(false) }

    val userData = viewModel.dataState.collectAsState()

    Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = nameTextState.value,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = if(userData.value.fullName.isEmpty()) stringResource(R.string.full_name) else userData.value.fullName)
            },
            onValueChange = { newValue ->
                nameTextState.value = newValue
                nameErrorState.value = false
            },
            isError = nameErrorState.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        AnimatedVisibility(nameErrorState.value, content = { ErrorText(stringResource(R.string.full_name_error)) })

        OutlinedTextField(
            value = phoneTextState.value,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = if(userData.value.phone.isEmpty()) stringResource(R.string.phone) else userData.value.phone) },
            onValueChange = { newValue ->
                phoneTextState.value = newValue
                phoneErrorState.value = false
            },
            isError = phoneErrorState.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        AnimatedVisibility(phoneErrorState.value, content = { ErrorText(stringResource(R.string.phone_error)) })

        Button(
            onClick = {
                when {
                    (Pattern.compile(".*\\d.*").matcher(nameTextState.value).matches() ||
                            nameTextState.value.length < 3) -> nameErrorState.value = true
                    phoneTextState.value.length < 9 -> phoneErrorState.value = true
                    else -> { viewModel.saveUserDate(nameTextState.value, phoneTextState.value) }
                }
            },
            enabled = viewModel.isUserActive
        ) {
            Text(text = stringResource(R.string.submit).uppercase())
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PasswordCardButton(
    viewModel: UserSettingsViewModel,
    buttonText: String,
    onClick: (Context, String, String) -> Unit)
= Card(
    shape = RoundedCornerShape(4.dp),
    elevation = 4.dp,
    modifier = Modifier.fillMaxWidth()
) {
    val visibility = remember { mutableStateOf(false) }

    val passwordTextState = remember { mutableStateOf("") }

    val newPasswordTextState = remember { mutableStateOf("") }

    val reNewPasswordTextState = remember { mutableStateOf("") }

    val context = LocalContext.current.applicationContext

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { visibility.value = !visibility.value },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.password), Modifier.fillMaxWidth(0.7f))
            Icon(
                painter = if (!visibility.value) painterResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                else painterResource(R.drawable.ic_baseline_keyboard_arrow_up_24),
                contentDescription = stringResource(id = R.string.password),
            )
        }
        AnimatedVisibility(visible = visibility.value) {
            Column(
                Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PasswordChangeField(viewModel, passwordTextState, stringResource(R.string.old_password))
                PasswordChangeField(viewModel, newPasswordTextState, stringResource(R.string.password))
                PasswordChangeField(viewModel, reNewPasswordTextState, stringResource(R.string.repeat_password))
                Button(
                    onClick = {
                        if(newPasswordTextState.value.length < 6 || reNewPasswordTextState.value.length < 6) viewModel.isPasswordEmpty.value = true
                        else if (newPasswordTextState.value != reNewPasswordTextState.value) viewModel.isPasswordNotMatch.value = true
                        else onClick(context, passwordTextState.value, reNewPasswordTextState.value)
                    },
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(text = buttonText.uppercase(), fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun PasswordChangeField(viewModel: UserSettingsViewModel, passwordTextState: MutableState<String>, label: String) =
    Column {
        val isPasswordEmpty = viewModel.isPasswordEmpty.collectAsState()

        val isPasswordInvalid = viewModel.isPasswordInvalid.collectAsState()

        val isPasswordNotMatch = viewModel.isPasswordNotMatch.collectAsState()

        val passwordVisibility = remember { mutableStateOf(false) }

        OutlinedTextField(
            value = passwordTextState.value,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = label) },
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