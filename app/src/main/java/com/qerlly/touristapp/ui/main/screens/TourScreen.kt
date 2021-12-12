package com.qerlly.touristapp.ui.main.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.android.material.composethemeadapter.MdcTheme
import com.qerlly.touristapp.R
import com.qerlly.touristapp.model.NewModel
import com.qerlly.touristapp.model.TourModel
import com.qerlly.touristapp.ui.main.Destinations
import com.qerlly.touristapp.viewModels.TourViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun TourScreen(navController: NavHostController) = MdcTheme {

    val viewModel = hiltViewModel<TourViewModel>()

    val tour = viewModel.tourState.collectAsState()

    val news = viewModel.tourNews.collectAsState()

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            Modifier
                .padding(16.dp)
                .height(260.dp)) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                painter = rememberImagePainter(tour.value?.image),
                contentDescription = stringResource(R.string.app_name),
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                text = tour.value?.title ?: stringResource(R.string.app_name),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                color = Color.White
            )
        }
        TourCardInfo(stringResource(R.string.details), tour.value)
        TourCard(stringResource(R.string.announcements), news)
        Button(onClick = {
            runBlocking { viewModel.leaveTour() }
            navController.navigate(Destinations.TOURS_SCREEN) {
            popUpTo(Destinations.TOUR_SCREEN) {
                inclusive = true
            }
        } }) {
            Text(text = stringResource(R.string.exit_tour).uppercase(),  style = MaterialTheme.typography.body1)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun TourCardInfo(title: String, tour: TourModel?) {

    val visibility = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
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
                    contentDescription = stringResource(id = R.string.tours),
                )
                Text(text = title,  style = MaterialTheme.typography.h6)
            }
            AnimatedVisibility(visible = visibility.value) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = tour?.description ?: "", fontSize = 14.sp)
                    Text(text = tour?.time ?: "", fontSize = 14.sp)
                    Text(text = tour?.gid ?: "", fontSize = 14.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun TourCard(title: String, info: State<List<NewModel>?>) {

    val visibility = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
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
                    contentDescription = stringResource(id = R.string.tours),
                )
                Text(text = title,  style = MaterialTheme.typography.h6)
            }
            AnimatedVisibility(visible = visibility.value) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    info.value?.forEach {
                        Text(text = it.info, fontSize = 14.sp, fontWeight = if (it.priority) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }
    }
}