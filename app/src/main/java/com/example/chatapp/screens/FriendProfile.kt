package com.example.chatapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chatapp.data.model.User
import com.example.chatapp.general.Constants
import com.example.chatapp.general.TransparentSurfaceWithGradient
import com.example.chatapp.general.selectFromTheme

@Composable
fun FriendProfile(
    friend: User,
    navigateToChat: () -> Unit,
) {
    val status = "Hey there! I'm using ChatTime"

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
                        onClick = navigateToChat,
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
                    Text(
                        text = "${friend.name}'s profile",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = friend.name.take(1).uppercase(),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = friend.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = status,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Customize profile:",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        textAlign = TextAlign.Start
                    )

                    TextButton(
                        onClick = { /* No functionality yet */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Change nickname",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    TextButton(
                        onClick = { /* No functionality yet */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Change chat theme",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }
}