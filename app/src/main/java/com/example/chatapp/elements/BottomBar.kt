import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.chatapp.general.Constants
import com.example.chatapp.general.TransparentSurfaceWithGradient
import com.example.chatapp.general.selectFromTheme


//package com.example.chatapp.elements
//
//import androidx.compose.runtime.Composable
//
@Composable
fun CustomBottomBar(
    modifier: Modifier = Modifier,
    alpha: Float = 0.42f,
    brush: Brush? = null,
    content: @Composable RowScope.() -> Unit = {}
) {
    TransparentSurfaceWithGradient(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        alpha = alpha,
        brush = brush ?: selectFromTheme(
            Brush.horizontalGradient(colors = Constants.Gradient.GREEN_TO_BROWN.reversed()),
            Brush.horizontalGradient(colors = Constants.Gradient.RED_TO_BLACK.reversed())
        ),
        border = null,
        roundedCornerShape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            content = content
        )
    }
}

