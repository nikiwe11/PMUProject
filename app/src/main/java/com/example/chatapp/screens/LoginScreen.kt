package com.example.chatapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.general.TransparentSurfaceWithGradient
import com.example.chatapp.R
import com.example.chatapp.elements.InputField
import com.example.chatapp.viewmodel.AppViewModelProvider
import com.example.chatapp.viewmodel.LoginScreenViewModel

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onClick: () -> Unit,
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // todo icon
                Text(text = stringResource(R.string.app_name))
                InputField(
                    value = uiState.loginDetails.userName,
                    onValueChange = {
                        viewModel.updateUiState(
                            uiState.loginDetails.copy(userName = it)
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
                        if (viewModel.validateUser()) {
                            onClick()
                        } else { // todo
                            viewModel.updateUiState(
                                uiState.loginDetails.copy(password = "1", userName = "1")
                            )
                        }
                    }
                ) {
                    Text("Login")
                }
            }
        }
    }
}