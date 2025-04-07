package com.example.chatapp.screens

import CustomBottomBar
import android.R
import android.R.id.message
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.data.model.Message
import com.example.chatapp.elements.ChatMessageItem
import com.example.chatapp.elements.InputField
import com.example.chatapp.general.Constants
import com.example.chatapp.general.NavigationDestination
import com.example.chatapp.general.TransparentSurfaceWithGradient
import com.example.chatapp.general.selectFromTheme
import com.example.chatapp.viewmodel.ChatScreenViewModel
import com.example.chatapp.viewmodel.CurrentUser
import java.time.format.DateTimeFormatter

object ChatDestination : NavigationDestination {
    override val route = Constants.Routes.CHAT
    override val titleRes = 3
    const val chatIdArg: String = "chatId"
    val routeWithArgs = "${route}/{$chatIdArg}"
}

@Composable
fun ChatScreen(
    viewModel: ChatScreenViewModel = hiltViewModel(),
    navigateToMainMenu: () -> Unit,
    navigateToFriendProfile: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()


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
                    // TODO: P-to t rqa bude zamesteno s profilna
                    ProfileIcon(
                        name = uiState.friendUser.name
                    )

                    TextButton(
                        onClick = navigateToFriendProfile,

                        )
                    {
                        Text(
                            text = uiState.friendUser.name,
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
                    value = uiState.messageDetails.message,
                    onValueChange = { viewModel.updateUiState(uiState.messageDetails.copy(message = it)) },
                    labelText = "Type here...",
                    containerColor = Color.Transparent,
                    textColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.75f),
                    focusedLabelColor = Color.White.copy(alpha = 0.9f),
                    border = null
                )

                // TODO: кат се натисне тряа може да се праща съобщението
                // todo can use IconButton
                TextButton(
                    onClick = { viewModel.sendMessage() },
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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val listState = rememberLazyListState()
            LaunchedEffect(uiState.messages.size) {
                // Scroll to the last item whenever the list size changes
                val size = uiState.messages.size
                if(size > 0){
                    listState.animateScrollToItem(uiState.messages.size - 1)
                }

            }
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),  // Takes available space
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)  // Space between items
            ) {
                items(uiState.messages) { message ->
                    val isFromCurrentUser = message.senderId == CurrentUser.id
                    ChatMessageItem(
                        message = message,
                        isFromCurrentUser = isFromCurrentUser,
                        senderName = viewModel.getSenderName(message)
                    )
                }
            }
        }
    }
}