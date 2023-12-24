package com.nttdatavds.musicplayer.presentation.screens.player.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PauseCircleOutline
import androidx.compose.material.icons.rounded.PlayCircleOutline
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nttdatavds.musicplayer.domain.model.Audio
import com.nttdatavds.musicplayer.presentation.screens.player.PlayerUiState
import com.nttdatavds.mediaplayer.presentation.ui.widgets.CircleRotationImage
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollapsedPlayerView(
    modifier: Modifier,
    uiState: PlayerUiState,
    onPlayPauseClick: () -> Unit,
    onNextBtnClick: () -> Unit,
    onPlayAudioChange: (Audio) -> Unit
) {

    val pagerState = rememberPagerState(pageCount = {
        uiState.playingAudios.size
    })


    Column {
        LinearProgressIndicator(
            progress = (uiState.playingProgress.toFloat() / uiState.audio!!.duration),
            modifier = Modifier.height(2.dp).fillMaxWidth(),
            backgroundColor = Color.DarkGray.copy(alpha = 0.8f)
        )
        Row(
            modifier = modifier
                .animateContentSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(7f)) {

                LaunchedEffect(pagerState.targetPage) {
                    snapshotFlow { pagerState.targetPage }
                        .distinctUntilChanged()
                        .collect { page ->
                            onPlayAudioChange(uiState.playingAudios[page])
                        }
                }

                LaunchedEffect(uiState.audio) {
                    val playingIndex = uiState.playingAudios.indexOf(uiState.audio)
                    if (playingIndex != -1) {
                        pagerState.scrollToPage(playingIndex)
                    }
                }

                HorizontalPager(
                    state = pagerState,
                ) { page ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                        ) {
                            CircleRotationImage(
                                modifier = modifier,
                                imageUri = uiState.playingAudios[page].artworkUri,
                                size = 35.dp,
                                isPlaying = uiState.isMediaPlaying,
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = uiState.playingAudios[page].title,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 17.sp,
                                textAlign = TextAlign.Start
                            ),
                            maxLines = 1,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }

            Column(
                modifier = Modifier.weight(3f),
                horizontalAlignment = Alignment.End
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                onPlayPauseClick()
                            }
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = if (!uiState.isMediaPlaying) Icons.Rounded.PlayCircleOutline else Icons.Rounded.PauseCircleOutline,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                onNextBtnClick()
                            }
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Rounded.SkipNext,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }

}