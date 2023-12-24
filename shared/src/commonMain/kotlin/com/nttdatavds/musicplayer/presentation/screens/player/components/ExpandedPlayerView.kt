package com.nttdatavds.musicplayer.presentation.screens.player.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.PauseCircleOutline
import androidx.compose.material.icons.rounded.PlayCircleOutline
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
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
import com.nttdatavds.musicplayer.platform.kmmStringFormat
import com.nttdatavds.musicplayer.presentation.screens.player.PlayerUiState
import com.nttdatavds.musicplayer.presentation.ui.theme.Purple100
import com.nttdatavds.mediaplayer.presentation.ui.widgets.CircleRotationImage

@Composable
fun ExpandedPlayerView(
    modifier: Modifier,
    uiState: PlayerUiState,
    onCollapsedClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextBtnClick: () -> Unit,
    onPreviousBtnClick: () -> Unit,
    onUpdateAudioProgress: (Long) -> Unit,
) {

    Box(modifier = modifier.background(Purple100)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.TopStart) {
                Icon(
                    modifier = Modifier
                        .size(35.dp)
                        .clickable {
                            onCollapsedClick()
                        },
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .weight(6f)
                    .clip(CircleShape), contentAlignment = Alignment.Center
            ) {
                CircleRotationImage(
                    modifier = modifier,
                    imageUri = uiState.audio!!.artworkUri,
                    size = 220.dp,
                    isPlaying = uiState.isMediaPlaying
                )
            }

            Column(
                Modifier
                    .weight(4f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text(
                        uiState.audio?.title ?: "",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))

                val totalDurationLabel = uiState.audio?.let {
                    val (minutes, seconds) = durationToMinutesSeconds(it.duration.toLong())
                    kmmStringFormat("%02d:%02d", minutes, seconds)
                }

                val currentDurationLabel = uiState.playingProgress.let {
                    val (minutes, seconds) = durationToMinutesSeconds(it)
                    kmmStringFormat("%02d:%02d", minutes, seconds)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        currentDurationLabel ?: "",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.weight(2f)
                    )
                    Slider(
                        value = (uiState.playingProgress).toFloat(),
                        valueRange = 0f..uiState.audio!!.duration.toFloat(),
                        onValueChange = {
                            onUpdateAudioProgress(it.toLong())
                        },
                        modifier = Modifier.weight(8f)
                    )
                    Text(
                        totalDurationLabel ?: "",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.weight(2f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .clickable {
                            },
                        imageVector = Icons.Rounded.Shuffle,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Icon(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .clickable {
                                onPreviousBtnClick()
                            },
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Icon(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .clickable {
                                onPlayPauseClick()
                            },
                        imageVector = if (!uiState.isMediaPlaying) Icons.Rounded.PlayCircleOutline else Icons.Rounded.PauseCircleOutline,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Icon(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .clickable {
                                onNextBtnClick()
                            },
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .clickable {
                            },
                        imageVector = Icons.Rounded.RepeatOne,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}