package com.example.chatapp.elements

import android.R.id.message
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ChatItem(
    profileIcon: ImageVector,
    name: String,
    lastMessage: String,
    isLastMessageFromMe: Boolean,
    date: String,
    time: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .padding(horizontal = 1.dp, vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Profile Icon
            ProfileIcon(
                name = name,
                modifier = Modifier.size(56.dp)
            )


            Spacer(modifier = Modifier.width(12.dp))

            // Name and Message
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                )
                Text(
                    text = if (isLastMessageFromMe) "You: $lastMessage" else lastMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Date and Time.
            Column(
                horizontalAlignment = Alignment.End
            ) {
                val (formattedDate, formattedTime) = remember(date) {
                    try {
                        val utcDateTime = OffsetDateTime.parse(date)
                        val localDateTime = utcDateTime.atZoneSameInstant(ZoneId.systemDefault())
                        val datePart = localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        val timePart = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                        Pair(datePart, timePart)
                    } catch (e: Exception) {
                        Pair("00.00.0000", "00:00")
                    }
                }

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Black)
                )
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Black)
                )
            }
        }
    }
}