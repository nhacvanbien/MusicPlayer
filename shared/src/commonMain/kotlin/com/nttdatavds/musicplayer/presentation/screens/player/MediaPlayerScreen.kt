package com.nttdatavds.musicplayer.presentation.screens.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.CurrentTab
import com.nttdatavds.musicplayer.presentation.screens.player.components.PlayerView
import com.nttdatavds.musicplayer.presentation.ui.theme.playerViewHeight

object MediaPlayerScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val viewModel: PlayerViewModel = getScreenModel<PlayerViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        val scaffoldState = rememberBottomSheetScaffoldState()
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = if (uiState.audio != null) playerViewHeight else 0.dp,
            sheetContent = {
                Box(
                    contentAlignment = Alignment.TopCenter
                ) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        //PlayerView
                        if (uiState.audio != null) {
                            val expandedHeight = this.maxHeight
                            val collapsedHeight = playerViewHeight
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                            ) {
                                PlayerView(
                                    modifier = Modifier.fillMaxWidth(),
                                    expandedHeight = expandedHeight,
                                    collapsedHeight = collapsedHeight,
                                    uiState = uiState,
                                    playerBottomSheetState = scaffoldState.bottomSheetState,
                                    onPlayPauseClick = {
                                        viewModel.handleUiEvent(PlayerUiEvent.PlayOrPauseEvent)
                                    },
                                    onNextBtnClick = {
                                        viewModel.handleUiEvent(PlayerUiEvent.NextEvent)
                                    },
                                    onPreviousBtnClick = {
                                        viewModel.handleUiEvent(PlayerUiEvent.PreviousEvent)
                                    },
                                    onPlayAudioChange = {
                                        viewModel.handleUiEvent(
                                            PlayerUiEvent.StartEvent(
                                                it,
                                                uiState.playingAudios
                                            )
                                        )
                                    },
                                    onUpdateAudioProgress = {
                                        viewModel.handleUiEvent(PlayerUiEvent.SeekToEvent(it))
                                    }
                                )
                            }
                        }
                    }
                }
            },
            content = {
                CurrentTab()
            }
        )
    }
}