package com.example.chatapp.screens

import CustomBottomBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatapp.elements.InputField
import com.example.chatapp.general.Constants
import com.example.chatapp.general.TransparentSurfaceWithGradient
import com.example.chatapp.general.selectFromTheme

@Composable
fun ChatScreen(
    navigateToMainMenu: () -> Unit,
    navigateToFriendProfile:() -> Unit,
) {
    Scaffold(
        topBar = {
            TransparentSurfaceWithGradient(
                modifier = Modifier.fillMaxWidth(),
                alpha = 0.42f,
                brush = selectFromTheme(
                    Brush.horizontalGradient(colors = Constants.Gradient.GREEN_TO_BROWN.reversed()),
                    Brush.horizontalGradient(colors = Constants.Gradient.RED_TO_BLACK.reversed())
                ),
                border = null,
                roundedCornerShape = RoundedCornerShape(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextButton(
                        onClick = navigateToMainMenu,
                        modifier = Modifier.padding(end = 8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            "<",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(horizontal = 3.dp)
                        )
                    }
                    // TODO: P-to trqa bude zamesteno s profilna  
                    Text(
                        "P" ,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 3.dp)
                    )

                    TextButton(
                        onClick =navigateToFriendProfile,

                    )
                    {Text(
                        text = "Penka Penova",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                    )
                    }
                }
            }
        },
        bottomBar = {
            CustomBottomBar {
                // TODO: тряа може да се пише в инпут фиилда
                InputField(
                    modifier = Modifier.weight(1f),
                    value = "",
                    onValueChange = {},
                    labelText = "Type here...",
                    containerColor = Color.Transparent,
                    textColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.75f),
                    focusedLabelColor = Color.White.copy(alpha = 0.9f),
                    border = null
                )

                // TODO: кат се натисне тряа може да се праща съобщението
                TextButton(
                    onClick = { /* No action */ },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        ">",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
        ) {
            // Chat messages content would go here
            LazyColumn(modifier = Modifier.weight(1f)) {
                // Chat items would go here
            }
        }
    }
}