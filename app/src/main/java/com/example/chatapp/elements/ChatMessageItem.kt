package com.example.chatapp.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.chatapp.data.model.Message
import com.example.chatapp.viewmodel.CurrentUser

@Composable
fun ChatMessageItem(
    message: Message,
    isFromCurrentUser: Boolean,
    senderName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isFromCurrentUser) {
            ProfileIcon(name = senderName)
        }

        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
        ) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isFromCurrentUser)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = senderName,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isFromCurrentUser)
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                            else
                                MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.9f)
                        )

                        val formattedDateTime = remember(message.timeStamp) {
                            try {
                                val datePart = message.timeStamp.take(10)
                                val timePart = message.timeStamp.substring(10).take(5)
                                "$datePart $timePart "
                            } catch (e: Exception) {
                                "00:00 0000-00-00"
                            }
                        }

                        Text(
                            text = formattedDateTime,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isFromCurrentUser)
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            else
                                MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }

                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isFromCurrentUser)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        if (isFromCurrentUser) {
            ProfileIcon(name = senderName)
        }
    }
}

