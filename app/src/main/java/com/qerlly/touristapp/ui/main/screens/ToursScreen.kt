package com.qerlly.touristapp.ui.main.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.qerlly.touristapp.R
import com.qerlly.touristapp.model.TourModel
import com.qerlly.touristapp.ui.main.Destinations
import com.qerlly.touristapp.viewModels.ToursViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun ToursScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<ToursViewModel>()

    val tours = viewModel.toursState.collectAsState()

    val textState = remember { mutableStateOf("") }

    val clearState = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = textState.value,
            modifier = Modifier.fillMaxWidth(0.7f),
            label = { Text(text = stringResource(R.string.search)) },
            onValueChange = {
                textState.value = it
                clearState.value = true
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            trailingIcon = {
                IconButton(
                    onClick = {
                    textState.value = ""
                    clearState.value = false
                    }) { if (clearState.value) { Icon(Icons.Filled.Clear, stringResource(R.string.search)) } }
            },
            maxLines = 1,
        )
        ToursList(tours, textState, viewModel, navController)
    }
}

@Composable
fun ToursList(
    tours: State<List<TourModel>?>,
    textState: MutableState<String>,
    viewModel: ToursViewModel,
    navController: NavHostController
) = Column(
    Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    tours.value?.filter { it.id.contains(textState.value) }?.forEach {
        TourListCard(it, viewModel, navController)
    }
}

@Composable
fun TourListCard(model: TourModel, viewModel: ToursViewModel, navController: NavHostController) = Card(
    shape = RoundedCornerShape(4.dp),
    elevation = 4.dp,
    modifier = Modifier.fillMaxWidth().height(120.dp)
) {
    val openDialog = remember { mutableStateOf(false)  }

    Row(Modifier.clickable { openDialog.value = true }) {
       Image(
           modifier = Modifier.fillMaxWidth(0.4f).fillMaxHeight().padding(2.dp),
           painter = rememberImagePainter(model.image),
           contentDescription = stringResource(R.string.tours)
       )
       Column {
           Text(
               modifier = Modifier.fillMaxWidth(),
               textAlign = TextAlign.Center,
               text = model.title,
               fontSize = 16.sp,
               maxLines = 1,
           )
           Text(
               modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f),
               textAlign = TextAlign.Center,
               text = model.description,
               fontSize = 10.sp,
               maxLines = 5,
           )
           Text(
               modifier = Modifier.fillMaxWidth().padding(top = 4.dp, end = 8.dp),
               textAlign = TextAlign.End,
               text = model.time,
               fontSize = 10.sp,
               fontStyle = FontStyle.Italic
           )
       }
    }
    DialogAdd(openDialog, model, viewModel, navController)
}

@Composable
fun DialogAdd(
    openDialog: MutableState<Boolean>,
    model: TourModel,
    viewModel: ToursViewModel,
    navController: NavHostController
) {

    val text = remember { mutableStateOf("") }

    val context = LocalContext.current

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = stringResource(R.string.tour_adding))
            },
            text = {
                TextField(
                    value = text.value,
                    onValueChange = { newValue -> text.value = newValue }
                )
                Text("Enter the pin code")
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if(text.value == model.password) {
                                runBlocking {viewModel.joinToTour(model.id) }
                                navController.navigate(Destinations.TOUR_SCREEN) {
                                    popUpTo(Destinations.TOURS_SCREEN) {
                                        inclusive = true
                                    }
                                }
                            } else { Toast.makeText(context, "Invalid pin", Toast.LENGTH_SHORT).show() }
                        }
                    ) {
                        Text("Apply")
                    }
                    Button(
                        onClick = { openDialog.value = false.also { text.value = "" } }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }
}