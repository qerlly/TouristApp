package com.qerlly.touristapp.ui.main.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qerlly.touristapp.R
import com.qerlly.touristapp.viewModels.ChatViewModel


@Composable
fun ChatScreen() = Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f)) {

    val viewModel = hiltViewModel<ChatViewModel>()

    val textState = remember { mutableStateOf("") }

    if(false) {
        InfoText(stringResource(R.string.need_tour_chat), Modifier.align(Alignment.Center).fillMaxWidth(0.7f))
    } else {
        if(false) {
            InfoText(stringResource(R.string.no_messages), Modifier.align(Alignment.Center).fillMaxWidth(0.7f))
        } else {
            ChatList()
        }
        OutlinedTextField(
            value = textState.value,
            modifier = Modifier.fillMaxWidth(0.7f).align(Alignment.BottomCenter),
            label = { Text(text = stringResource(R.string.message)) },
            onValueChange = { textState.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            trailingIcon = {
                IconButton(
                    onClick = { textState.value = "" }) {
                    Icon(Icons.Filled.Send, stringResource(R.string.send))
                }
            },
            maxLines = 1,
        )
    }
}

@Composable
fun ChatList() = Column(
    modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.85f)
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
    ChatItem()
}

@Composable
fun ChatItem() = Column {
    Text(
        text = "Mike Penz",
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Serif,
        color = MaterialTheme.colors.primary,
        style = MaterialTheme.typography.body1
    )
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        modifier = Modifier.padding(4.dp).defaultMinSize(64.dp, 32.dp)
    ) {
        Text("Hello Mike, How are you?", Modifier.padding(16.dp))
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

