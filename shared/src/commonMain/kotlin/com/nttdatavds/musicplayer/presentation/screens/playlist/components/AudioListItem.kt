package com.nttdatavds.musicplayer.presentation.screens.playlist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nttdatavds.musicplayer.core.utils.durationToMinutesSeconds
import com.nttdatavds.musicplayer.domain.model.Audio
import com.nttdatavds.musicplayer.platform.kmmStringFormat
import com.seiko.imageloader.rememberAsyncImagePainter

@Composable
fun AudioListItem(
    modifier: Modifier,
    index: Int,
    audio: Audio,
    isPlaying: Boolean,
    onAudioClick: (Audio) -> Unit,
) {

    val totalDurationLabel = audio.let {
        val (minutes, seconds) = durationToMinutesSeconds(it.duration.toLong())
        kmmStringFormat("%02d:%02d", minutes, seconds)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(if (isPlaying) 8.dp else 0.dp))
            .background(if (isPlaying) Color(0XFF3a3344) else Color.Transparent)
            .clickable { onAudioClick(audio) }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberAsyncImagePainter(audio.artworkUri),
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .width(60.dp)
                .height(60.dp)
                .weight(1f),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(4f)) {
            Row {
                Text(
                    "$index",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    ),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    audio.title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 17.sp
                    ),
                    maxLines = 1
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            totalDurationLabel,
            modifier = Modifier.height(20.dp)
                .weight(1f),
            style = TextStyle(
                color = Color.White,
                fontSize = 15.sp,
                textAlign = TextAlign.End
            ),
        )
    }
}