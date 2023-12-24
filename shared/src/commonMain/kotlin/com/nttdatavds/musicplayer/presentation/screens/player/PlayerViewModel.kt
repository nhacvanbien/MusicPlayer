package com.nttdatavds.musicplayer.presentation.screens.player

import cafe.adriel.voyager.core.model.screenModelScope
import com.nttdatavds.musicplayer.core.base.presentation.viewmodel.BaseViewModel
import com.nttdatavds.musicplayer.domain.model.Audio
import com.nttdatavds.musicplayer.platform.player.AudioState
import com.nttdatavds.musicplayer.platform.player.MediaControlEvent
import com.nttdatavds.musicplayer.platform.player.MediaPlayerController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerController: MediaPlayerController
) :
    BaseViewModel<PlayerUiState, PlayerUiEvent>() {

    init {
        listenAudiStateChange()
    }

    override fun initState(): PlayerUiState {
        return PlayerUiState()
    }

    private fun listenAudiStateChange() {
        screenModelScope.launch {
            playerController.audioStates.collectLatest { audioState: AudioState ->
                when (audioState) {
                    is AudioState.CurrentAudioChange -> {
                        val playingAudio =
                            _uiState.value.playingAudios.first { audio: Audio -> audio.uri == audioState.uri }
                        _uiState.update { it.copy(audio = playingAudio) }
                    }

                    is AudioState.MediaPlaying -> {
                        _uiState.update { it.copy(isMediaPlaying = audioState.isPlaying) }
                    }

                    is AudioState.ProgressChanged -> {
                        _uiState.update { it.copy(playingProgress = audioState.progress) }
                    }

                    else -> {/* no-op */
                    }
                }
            }
        }
    }


    override fun handleUiEvent(event: PlayerUiEvent) {
        when (event) {
            is PlayerUiEvent.StartEvent -> {
                _uiState.value.playingAudios = event.audios
                playerController.onMediaControlEvent(
                    MediaControlEvent.StartEvent(
                        event.audio,
                        event.audios
                    )
                )
            }

            is PlayerUiEvent.PlayOrPauseEvent -> {
                playerController.onMediaControlEvent(MediaControlEvent.PlayOrPauseEvent)
            }

            is PlayerUiEvent.PreviousEvent -> {
                playerController.onMediaControlEvent(MediaControlEvent.SeekToPreviousEvent)
            }

            is PlayerUiEvent.NextEvent -> {
                playerController.onMediaControlEvent(MediaControlEvent.SeekToNextEvent)
            }

            is PlayerUiEvent.SeekToEvent -> {
                playerController.onMediaControlEvent(MediaControlEvent.SeekToEvent(event.position))
            }
        }
    }

}