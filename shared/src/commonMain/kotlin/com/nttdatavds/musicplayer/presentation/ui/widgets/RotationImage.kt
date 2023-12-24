package com.nttdatavds.mediaplayer.presentation.ui.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.seiko.imageloader.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@Composable
fun CircleRotationImage(
    modifier: Modifier, size: Dp,
    imageUri: String,
    isPlaying: Boolean
) {
    val angle = remember {
        Animatable(0f)
    }
    LaunchedEffect(isPlaying) {
        launch {
            if (isPlaying) {
                angle.animateTo(
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        tween(20000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        }
    }

    Image(
        painter = rememberAsyncImagePainter(imageUri),
        contentDescription = null,
        modifier = Modifier
            .clip(CircleShape)
            .width(size)
            .height(size)
            .graphicsLayer { rotationZ = angle.value }
    )
}