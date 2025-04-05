package com.example.chatapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.R
import com.example.chatapp.elements.InputField
import com.example.chatapp.general.TransparentSurfaceWithGradient
import com.example.chatapp.viewmodel.LoginScreenViewModel

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = hiltViewModel(),
    navigateToMainMenu: () -> Unit,
    navigateToSignUp: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    // todo fix layout
    // Logic for each element


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TransparentSurfaceWithGradient {
            Column(
                modifier=Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)

            ) {
                // todo icon
                Text(text = stringResource(R.string.app_name))
                InputField(
                    value = uiState.loginDetails.email,
                    onValueChange = {
                        viewModel.updateUiState(
                            uiState.loginDetails.copy(email = it)
                        )
                    },
                    labelText = "Username or Email"
                )
                InputField(
                    value = uiState.loginDetails.password,
                    onValueChange = {
                        viewModel.updateUiState(
                            uiState.loginDetails.copy(password = it)
                        )
                    },
                    labelText = "Password"
                )
                Button(
                    onClick = {
                        viewModel.attemptLogin(onSuccess = { navigateToMainMenu() }, onFail = {})
                    }
                ) {
                    Text("Login")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Don't have an account?")
                    ClickableText(
                        text = AnnotatedString("Sign up"),
                        onClick = { navigateToSignUp() })
                }
            }
        }
    }
}