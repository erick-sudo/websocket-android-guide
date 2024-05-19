package com.whisper.websocketguide.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.whisper.websocketguide.R
import com.whisper.websocketguide.api.AccessTokenRequest
import com.whisper.websocketguide.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun Login(
    authViewModel: AuthViewModel
) {

    var loginCredentials by remember {
        mutableStateOf(AccessTokenRequest("", ""))
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier,
            value = loginCredentials.username,
            onValueChange = { loginCredentials = loginCredentials.copy(username = it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            label = { Text("Enter your username") },
            trailingIcon = {
                Icon(imageVector = Icons.Outlined.Person, contentDescription = "Username")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier,
            value = loginCredentials.password,
            onValueChange = { loginCredentials = loginCredentials.copy(password = it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            label = { Text("Password") },
            trailingIcon = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.outline_remove_red_eye_24), contentDescription = "Password")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            enabled = !(loginCredentials.username.isEmpty() || loginCredentials.username.isEmpty()),
            contentPadding = PaddingValues(vertical = 15.dp, horizontal = 60.dp),
            onClick = {
                coroutineScope.launch {
                    authViewModel.login(loginCredentials)
                }
            }
        ) {
            Text(text = "Submit")
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {

}