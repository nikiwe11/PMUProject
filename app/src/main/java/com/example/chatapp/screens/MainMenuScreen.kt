package com.example.chatapp.screens

import CustomBottomBar
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.data.model.User
import com.example.chatapp.elements.ChatItem
import com.example.chatapp.general.Constants
import com.example.chatapp.general.TransparentSurfaceWithGradient
import com.example.chatapp.general.selectFromTheme
import com.example.chatapp.viewmodel.CurrentUser
import com.example.chatapp.viewmodel.MainMenuViewModel
import com.example.chatapp.R
import java.util.concurrent.locks.Lock

@Composable
fun MainMenuScreen(
    viewModel: MainMenuViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateToAddFriend: () -> Unit,
    navigateToChat: (String) -> Unit,
    navigateToProfile: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val isLoading = true

    if (!uiState.userIsLogged) {
        navigateToLogin()
    }
    if (uiState.loading) {
        CircularProgressIndicator()
    } else {
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {

                        Image(
                            painter = painterResource(R.drawable.chat_time_icon),
                            contentDescription = null,
                            modifier = Modifier.size(45.dp)
                        )



                        Text(
                            "ChatTime",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = Color.White
                        )
                        // Right side - Add button
                        TextButton(
                            onClick = navigateToAddFriend,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                "Add",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            bottomBar = {
                CustomBottomBar {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chats",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = { navigateToProfile() }) {
                            Text(
                                text = "Profile",
                                modifier = Modifier.padding(horizontal = 8.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Friends label
                    Text(
                        text = "Friends:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Icons row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(CurrentUser.friends) { friend ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { navigateToChat(friend.id) }
                                ) {
                                    ProfileIcon(name = friend.name)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = friend.name,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                    LazyColumn (content = {
                        Log.d("GoshoC","Friends=${CurrentUser.friends}")
                        items(CurrentUser.friends) { friend ->
                            val chat = viewModel.getChatByFriendId(friend.id)
                            if(chat!=null){
                                ChatItem(
                                    profileIcon = Icons.Default.Person,
                                    name = friend.name,
                                    lastMessage = chat.lastMessage.text,
                                    date = chat.lastMessage.timeStamp,
                                    isLastMessageFromMe = false,
                                    time = chat.lastMessage.timeStamp,
                                    onClick = { navigateToChat(friend.id) }
                                )
                            }

                        }
                    })


                    // TODO: кат се направят чатовете да се използва за да те изпрати към тях
//                    ChatItem(
//                        profileIcon = Icons.Default.Person,
//                        name = "Иванка",
//                        lastMessage = "Zdravei!!",
//                        date = "Днес",
//                        time = "12:34",
//                        isLastMessageFromMe = false,
//                        onClick = { }
//                    )
//
//                    ChatItem(
//                        profileIcon = Icons.Default.Person,
//                        name = "Гошо",
//                        lastMessage = "Как си xD",
//                        date = "20/10/2024",
//                        time = "11:10",
//                        onClick = { navigateToChat() }
//                    )

                    // TODO: кат се направят чатовете да се използва за да те изпрати към тях
//                    Button(
//                        onClick = { navigateToChat() },
//                        modifier = Modifier
//                            .align(Alignment.Start)
//                            .padding(top = 8.dp)
//                    ) {
//                        Text("Chat")
//                    }

                    Spacer(modifier = Modifier.weight(1f))

//                    Row(
//                        horizontalArrangement = Arrangement.Start,
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        // TODO: trqa se mahne sign out butona i da ide na ProfileScreen
//                        Button(
//                            onClick = { viewModel.signOut() },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 8.dp)
//                        ) {
//                            Text("sign out")
//                        }
//                    }
                }
            }
        }
    }

}

@Composable
fun ProfileIcon(name: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.take(1).uppercase(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}