package com.nttdatavds.musicplayer.presentation.screens.playlist.remoteplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.nttdatavds.musicplayer.core.base.presentation.ui.ResourceUIContainer
import com.nttdatavds.musicplayer.presentation.screens.player.PlayerUiEvent
import com.nttdatavds.musicplayer.presentation.screens.player.PlayerViewModel
import com.nttdatavds.musicplayer.presentation.screens.playlist.PlayListUiEvent
import com.nttdatavds.musicplayer.presentation.screens.playlist.components.PlayList
import com.nttdatavds.musicplayer.presentation.ui.theme.Purple100
import com.nttdatavds.musicplayer.presentation.ui.theme.Purple95
import com.nttdatavds.musicplayer.presentation.ui.theme.playerViewHeight

object RemotePlayListScreen : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.BrowseGallery)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Browse",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val playListViewModel: RemotePlayListViewModel = getScreenModel<RemotePlayListViewModel>()
        val playListUiState by playListViewModel.uiState.collectAsState()

        val playerViewModel: PlayerViewModel = getScreenModel<PlayerViewModel>()
        val playerUiState by playerViewModel.uiState.collectAsState()

        ResourceUIContainer(
            modifier = Modifier
                .fillMaxSize()
                .background(Purple100),
            resourceUiState = playListUiState.audios,
            successView = { audios ->
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Purple95)
                        .padding(bottom = if (playerUiState.audio != null) playerViewHeight else 0.dp)
                ) {
                    PlayList(
                        modifier = Modifier.fillMaxSize(),
                        audios = audios,
                        playingAudioUri = playListUiState.playingAudioUri,
                        onAudioItemClick = {
                            playerViewModel.handleUiEvent(
                                PlayerUiEvent.StartEvent(
                                    it,
                                    audios
                                )
                            )
                        }
                    )
                }
            },
            onTryAgainWhenError = {
                playListViewModel.handleUiEvent(PlayListUiEvent.TryToLoadAudiosAgain)
            },
            onCheckAgainWhenEmpty = {
                playListViewModel.handleUiEvent(PlayListUiEvent.TryToLoadAudiosAgain)
            }
        )
    }
}
