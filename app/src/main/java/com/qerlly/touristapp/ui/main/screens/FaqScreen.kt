package com.qerlly.touristapp.ui.main.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.composethemeadapter.MdcTheme
import com.qerlly.touristapp.R
import com.qerlly.touristapp.model.FaqModel
import com.qerlly.touristapp.viewModels.FaqViewModel

@Composable
fun FaqScreen() = MdcTheme {

    val viewModel = hiltViewModel<FaqViewModel>()

    val faqState = viewModel.faqState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        if (faqState.value == null) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            FaqList(faqState)
        }
    }
}

@Composable
fun FaqList(faqState: State<List<FaqModel>?>) = Column {
    faqState.value?.forEach {
        FaqCard(it)
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun FaqCard(faq: FaqModel) {

    val visibility = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        onClick = { visibility.value = !visibility.value }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = if (!visibility.value) painterResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    else painterResource(R.drawable.ic_baseline_keyboard_arrow_up_24),
                    contentDescription = stringResource(id = R.string.faq),
                )
                Text(text = faq.question,  style = MaterialTheme.typography.h6)
            }
            AnimatedVisibility(visible = visibility.value) { Text(text = faq.answer, fontSize = 14.sp) }
        }
    }
}