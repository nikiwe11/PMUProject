package com.example.chatapp.general

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A reusable container that has a transparent background with a gradient. Contents remain fully opaque.
 */

@Composable
fun TransparentSurfaceWithGradient(
    modifier: Modifier = Modifier,
    brush: Brush = Brush.horizontalGradient(
        colors = selectFromTheme(
            lightThemeValue = Constants.Gradient.GRAY_TO_WHITE,
            darkThemeValue = Constants.Gradient.BLACK_TO_GRAY.reversed()
        )
    ),
    useGradient: Boolean = true,
    alpha: Float = selectFromTheme(lightThemeValue = 0.23f, darkThemeValue = 0.5f),
    color: Color = selectFromTheme(Color.White, Color.Black),
    border: BorderStroke? = selectFromTheme(
        lightThemeValue = BorderStroke(
            1.dp,
            color = Color.Gray
        ), darkThemeValue = null
    ),
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(16.dp),
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        Surface(
            color = color,
            modifier = Modifier
                .matchParentSize()
                .alpha(alpha)
                .clip(roundedCornerShape)
                .let {
                    if (border != null) it.border(
                        border,
                        shape = roundedCornerShape
                    ) else it
                },
        ) {
            if (useGradient) {
                Box(modifier = Modifier.background(brush))
            }
        }
        content()
    }
}