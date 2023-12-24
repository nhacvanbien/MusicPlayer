package com.nttdatavds.musicplayer.presentation.screens.player.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.nttdatavds.musicplayer.domain.model.Audio
import com.nttdatavds.musicplayer.presentation.screens.player.PlayerUiState
import com.nttdatavds.musicplayer.presentation.ui.theme.Purple100
import com.nttdatavds.musicplayer.presentation.ui.theme.playerViewHeight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerView(
    modifier: Modifier,
    uiState: PlayerUiState,
    playerBottomSheetState: BottomSheetState,
    expandedHeight: Dp,
    collapsedHeight: Dp = playerViewHeight,
    onPlayPauseClick: () -> Unit,
    onNextBtnClick: () -> Unit,
    onPreviousBtnClick: () -> Unit,
    onUpdateAudioProgress: (Long) -> Unit,
    onPlayAudioChange: (Audio) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var expanded by rememberSaveable {
        mutableStateOf(playerBottomSheetState.isExpanded)
    }

    //TODO check this launched effect
    LaunchedEffect(playerBottomSheetState.progress) {
        expanded =
            playerBottomSheetState.targetValue == BottomSheetValue.Expanded && playerBottomSheetState.progress > 0.9f
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        ExpandedPlayerView(
            modifier = Modifier
                .background(Purple100)
                .height(expandedHeight),
            uiState = uiState,
            onCollapsedClick = {
                expanded = false
                coroutineScope.launch {
                    playerBottomSheetState.collapse()
                }
            },
            onPlayPauseClick = onPlayPauseClick,
            onNextBtnClick = onNextBtnClick,
            onPreviousBtnClick = onPreviousBtnClick,
            onUpdateAudioProgress = onUpdateAudioProgress
        )

//        AnimatedVisibility(
//            !expanded,
//            enter = fadeIn() + slideInVertically(),
//            exit = fadeOut()
//        ) {
            CollapsedPlayerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Purple100)
                    .height(collapsedHeight)
                    .clickable {
                        expanded = true
                        coroutineScope.launch {
                            playerBottomSheetState.expand()
                        }
                    },
                uiState = uiState,
                onPlayPauseClick = onPlayPauseClick,
                onNextBtnClick = onNextBtnClick,
                onPlayAudioChange = onPlayAudioChange,
            )
//        }
    }
}