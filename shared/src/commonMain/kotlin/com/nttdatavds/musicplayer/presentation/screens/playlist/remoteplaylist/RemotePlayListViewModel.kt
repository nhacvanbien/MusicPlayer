package com.nttdatavds.musicplayer.presentation.screens.playlist.remoteplaylist

import cafe.adriel.voyager.core.model.screenModelScope
import com.nttdatavds.musicplayer.core.base.presentation.ui.ResourceUiState
import com.nttdatavds.musicplayer.core.base.presentation.viewmodel.BaseViewModel
import com.nttdatavds.musicplayer.domain.usecase.GetRemoteMediasUseCase
import com.nttdatavds.musicplayer.platform.player.AudioState
import com.nttdatavds.musicplayer.platform.player.MediaPlayerController
import com.nttdatavds.musicplayer.presentation.screens.playlist.PlayListUiEvent
import com.nttdatavds.musicplayer.presentation.screens.playlist.PlayListUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RemotePlayListViewModel(
    val getRemoteMediasUseCase: GetRemoteMediasUseCase,
    val playerController: MediaPlayerController,
) : BaseViewModel<PlayListUiState, PlayListUiEvent>() {

    override fun initState(): PlayListUiState {
        return PlayListUiState(ResourceUiState.Init)
    }

    init {
        getAudios()
        listenAudioStateChange()
    }

    override fun handleUiEvent(event: PlayListUiEvent) {
        when (event) {
            PlayListUiEvent.TryToLoadAudiosAgain -> {
                getAudios()
            }
        }
    }

    private fun getAudios() {
        _uiState.update { it.copy(audios = ResourceUiState.Loading) }
        screenModelScope.launch {
            getRemoteMediasUseCase()
                .onSuccess { audios ->
                    if (audios.isEmpty()) {
                        _uiState.update { it.copy(audios = ResourceUiState.Empty) }
                    } else {
                        _uiState.update { it.copy(audios = ResourceUiState.Success(audios)) }
                    }
                }.onFailure {
                    _uiState.update { it.copy(audios = ResourceUiState.Error()) }
                }
        }
    }

    private fun listenAudioStateChange() {
        screenModelScope.launch {
            playerController.audioStates.collectLatest { audioState: AudioState ->
                when (audioState) {
                    is AudioState.CurrentAudioChange -> {
                        _uiState.update { it.copy(playingAudioUri = audioState.uri) }
                    }

                    else -> {/* no-op */
                    }
                }
            }
        }

    }
}