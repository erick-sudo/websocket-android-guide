package com.whisper.websocketguide.ui.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whisper.websocketguide.models.HiveConversation
import com.whisper.websocketguide.models.HiveMessage

@Composable
fun ConversationScreen(
    conversation: HiveConversation,
    onBack: () -> Unit,
    onNewMessage: (text: String, conversationId: Int) -> Unit
) {

    var text by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back to conversation listings"
                )
            }
            Text(
                text = conversation.title,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            LazyColumn {
                items(conversation.messages) { hiveMessage ->
                    Row(
                    ) {
                        if(hiveMessage.userId == 1) Box(modifier = Modifier.weight(0.15f))
                        Column(
                            modifier = Modifier
                                .weight(0.85f)
                                .padding(5.dp),
                            horizontalAlignment = when(hiveMessage.userId == 1) {
                                true -> Alignment.End
                                else -> Alignment.Start
                            }
                        ) {
                            Card(
                                modifier = Modifier,
                                colors = when(hiveMessage.userId == 1) {
                                    true -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                                    else -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                                }
                            ) {
                                Text(
                                    text = hiveMessage.text,
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp, top = 15.dp)
                                )
                                Text(
                                    text = SimpleDateFormat.getInstance().format(hiveMessage.createdAt),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
                                )
                            }
                        }
                        if(hiveMessage.userId != 1) Box(modifier = Modifier.weight(0.15f))
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = text,
                placeholder = { Text(text = "Message") },
                shape = RoundedCornerShape(50),
                onValueChange = { text = it },
                leadingIcon = { Icon(imageVector = Icons.Outlined.Face, contentDescription = "Emoji") }
            )
            Spacer(modifier = Modifier.width(5.dp))
            Button(
                enabled = text.isNotEmpty(),
                onClick = {
                    onNewMessage(text, conversation.id)
                    text = ""
                }
            ) {
                Text(text = "Send")
            }
        }
    }
}

val messages = listOf(
    HiveMessage(1, "Hellow", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:07:20.628Z"), 1, 1),
    HiveMessage(2, "Morning", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:11:56.780Z"), 1, 2),
    HiveMessage(3, "Morning to you", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:12:14.259Z"), 1, 1),
    HiveMessage(4, "Umenyamazia nani", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:12:27.669Z"), 1, 1),
    HiveMessage(5, "Ntawapiga wote", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:12:43.778Z"), 1, 2),
    HiveMessage(6, "Yooh", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:15:03.776Z"), 1, 2),
    HiveMessage(7, "Hey", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:22:28.585Z"), 1, 2),
    HiveMessage(8, "Yes Hi", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:22:36.155Z"), 1, 1),
    HiveMessage(9, "He sells sea shells at the sea shore", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:29:40.530Z"), 1, 1),
    HiveMessage(10, "The quick brown fox jumped over the lazy dog", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:30:07.937Z"), 1, 2),
    HiveMessage(11, " Start chatting with your friends, colleagues, or anyone you'd like. Feel free to create new conversations, send messages, and express yourself.", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:37:22.786Z"), 1, 1),
    HiveMessage(12, " Start chatting with your friends, colleagues, or anyone you'd like. Feel free to create new conversations, send messages, and express yourself.", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:37:30.556Z"), 1, 1),
    HiveMessage(13, "Alright", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:38:05.875Z"), 1, 2),
    HiveMessage(14, "Hello", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:48:31.112Z"), 1, 1),
    HiveMessage(15, "yes", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-17T21:49:06.628Z"), 1, 2),
    HiveMessage(16, "Herooo", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T08:18:59.113Z"), 1, 1),
    HiveMessage(17, "Okay", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T08:19:38.292Z"), 1, 1),
    HiveMessage(18, "Okay", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T08:20:27.899Z"), 1, 2),
    HiveMessage(19, "Okay2", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T08:22:22.400Z"), 1, 2),
    HiveMessage(20, "Testing job", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T08:29:55.078Z"), 1, 2),
    HiveMessage(21, "Yooh", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T08:51:19.571Z"), 1, 1),
    HiveMessage(31, "Yeah", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T10:38:07.118Z"), 1, 2),
    HiveMessage(35, "onge bwana", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T10:52:32.893Z"), 1, 2),
    HiveMessage(36, "odhi kanye", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T10:52:45.868Z"), 1, 2),
    HiveMessage(37, "dong koro", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T10:52:59.061Z"), 1, 1),
    HiveMessage(38, "Hello", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T11:45:58.337Z"), 1, 1),
    HiveMessage(39, "Active record", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T11:46:09.592Z"), 1, 1),
    HiveMessage(40, "Action Cable", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T11:46:29.362Z"), 1, 1),
    HiveMessage(41, "Sidekiq", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T11:47:23.560Z"), 1, 2),
    HiveMessage(42, "Async adapter", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T11:48:09.100Z"), 1, 2),
    HiveMessage(44, "Hall", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T11:48:45.240Z"), 1, 1),
    HiveMessage(50, "Hey", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T15:06:00.888Z"), 1, 2),
    HiveMessage(51, "niaje", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T15:06:13.721Z"), 1, 1),
    HiveMessage(57, "He sells sea shells at the sea shore", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T15:14:16.589Z"), 1, 2),
    HiveMessage(58, "The quick brown fox jumped over the lazy dog. The quick brown fox jumped over the lazy dog.", false, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2024-05-18T15:15:45.062Z"), 1, 2)
)

@Preview(showSystemUi = true)
@Composable
fun ConversationScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            LazyColumn {
                items(messages) { hiveMessage ->
                    Row(
                    ) {
                        if(hiveMessage.userId == 1) Box(modifier = Modifier.weight(0.15f))
                        Column(
                            modifier = Modifier
                                .weight(0.85f)
                                .padding(5.dp),
                            horizontalAlignment = when(hiveMessage.userId == 1) {
                                true -> Alignment.End
                                else -> Alignment.Start
                            }
                        ) {
                            Card(
                                modifier = Modifier,
                                colors = when(hiveMessage.userId == 1) {
                                    true -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                                    else -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                                }
                            ) {
                                Text(
                                    text = hiveMessage.text,
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp, top = 15.dp)
                                )
                                Text(
                                    text = SimpleDateFormat.getInstance().format(hiveMessage.createdAt),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
                                )
                            }
                        }
                        if(hiveMessage.userId != 1) Box(modifier = Modifier.weight(0.15f))
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = "",
                placeholder = { Text(text = "Message") },
                shape = RoundedCornerShape(50),
                onValueChange = {},
                leadingIcon = { Icon(imageVector = Icons.Outlined.Face, contentDescription = "Emoji") }
            )
            Spacer(modifier = Modifier.width(5.dp))
            Button(onClick = {

            }) {
                Text(text = "Send")
            }
        }
    }
}