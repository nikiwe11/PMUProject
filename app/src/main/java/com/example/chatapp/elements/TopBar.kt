import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chatapp.general.Constants
import com.example.chatapp.general.TransparentSurfaceWithGradient
import com.example.chatapp.general.selectFromTheme

@Composable
fun CustomTopBar(
    currentScreenName: String,
) {

    TransparentSurfaceWithGradient(
        modifier = Modifier.fillMaxWidth(),
        alpha = 0.42f,
        brush = selectFromTheme(
            Brush.horizontalGradient(
                colors = Constants.Gradient.GREEN_TO_BROWN.reversed(),
            ), Brush.horizontalGradient(
                colors = Constants.Gradient.RED_TO_BLACK.reversed()
            )
        ),
        border = null,
        roundedCornerShape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val text = currentScreenName
            Text(
                modifier = Modifier.alpha(1f), text = text, style = if (text.length < 23) {
                    MaterialTheme.typography.headlineSmall
                } else {
                    MaterialTheme.typography.titleLarge
                }, textAlign = TextAlign.Center, color = Color.White
            )
        }
    }
}