package com.qerlly.touristapp.ui.main.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.qerlly.touristapp.R
import com.qerlly.touristapp.model.MessageModel
import com.qerlly.touristapp.viewModels.ChatViewModel
import kotlin.math.roundToInt

@Composable
fun ChatScreen(photo: () -> Unit, isGid: Boolean) =
    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f)) {

        val viewModel = hiltViewModel<ChatViewModel>()

        val textState = remember { mutableStateOf("") }

        val messages = viewModel.messages.collectAsState()

        val context = LocalContext.current

        val listState = rememberScrollState()

        val (submitListPosition, setSubmitListPosition) = remember { mutableStateOf(0f) }

        when {
            messages.value == null -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            messages.value!!.isEmpty() -> {
                InfoText(
                    stringResource(R.string.no_messages),
                    Modifier.align(Alignment.Center).fillMaxWidth(0.7f)
                )
            }
            else -> {
                LaunchedEffect(submitListPosition) {
                    listState.animateScrollTo(submitListPosition.roundToInt())
                }
                ChatList(messages.value!!, listState, setSubmitListPosition)
            }
        }

        Row(Modifier.align(Alignment.BottomCenter), verticalAlignment = Alignment.CenterVertically) {
            if(isGid) {
                IconButton(onClick = { photo() })
                { Icon(Icons.Filled.PhotoCamera, stringResource(R.string.send), Modifier.size(32.dp)) }
            }
            OutlinedTextField(
                value = textState.value,
                modifier = Modifier.fillMaxWidth(0.7f),
                label = { Text(text = stringResource(R.string.message)) },
                onValueChange = { textState.value = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (textState.value.isEmpty()) Toast.makeText(context, R.string.no_text, Toast.LENGTH_SHORT).show()
                            else { viewModel.sendMessage(textState.value).also { textState.value = "" } }
                        }) { Icon(Icons.Filled.Send, stringResource(R.string.send)) }
                },
                maxLines = 1,
            )
        }
    }

@Composable
fun ChatList(
    value: List<MessageModel>,
    listState: ScrollState,
    setSubmitListPosition: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .verticalScroll(listState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) { value.forEach {
            ChatItem(it, setSubmitListPosition)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ChatItem(messageModel: MessageModel, onPositionChange: (Float) -> Unit) = Column(
    modifier = Modifier.fillMaxWidth().onGloballyPositioned { onPositionChange(it.positionInParent().y) },
    horizontalAlignment = if (messageModel.own) Alignment.End else Alignment.Start
) {
    Text(
        text = messageModel.email,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Serif,
        color = if (messageModel.email.endsWith("@firma.gid.com")) Color.Red else MaterialTheme.colors.primary,
        style = MaterialTheme.typography.body2
    )
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        modifier = Modifier.padding(4.dp)
    ) {
        if (messageModel.isImage) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(320.dp)
                    .clip(RoundedCornerShape(10.dp)),
                painter = rememberImagePainter(messageModel.message),
                contentDescription = stringResource(R.string.app_name),
            )
        } else {
            Column(horizontalAlignment = if (messageModel.own) Alignment.End else Alignment.Start) {
                Text(messageModel.message, Modifier.padding(16.dp))
                Text(
                    messageModel.time, Modifier.padding(start = 16.dp, bottom = 16.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun InfoText(stringResource: String, modifier: Modifier) = Text(
    text = stringResource,
    modifier = modifier,
    textAlign = TextAlign.Center,
    fontFamily = FontFamily.Serif,
    color = MaterialTheme.colors.primary,
    style = MaterialTheme.typography.h6
)