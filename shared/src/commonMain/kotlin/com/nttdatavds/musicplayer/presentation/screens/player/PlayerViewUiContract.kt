package com.nttdatavds.musicplayer.presentation.screens.player

import com.nttdatavds.musicplayer.core.base.presentation.viewmodel.UiEvent
import com.nttdatavds.musicplayer.core.base.presentation.viewmodel.UiState
import com.nttdatavds.musicplayer.domain.model.Audio

sealed class PlayerUiEvent : UiEvent {
    class StartEvent(val audio: Audio, val audios: List<Audio>) : PlayerUiEvent()
    data object PlayOrPauseEvent : PlayerUiEvent()
    data object NextEvent : PlayerUiEvent()
    data object PreviousEvent : PlayerUiEvent()
    class SeekToEvent(val position: Long) : PlayerUiEvent()
}

data class PlayerUiState(
    val isMediaPlaying: Boolean = false,
    val audio: Audio? = null,
    var playingAudios: List<Audio> = emptyList(),
    val playingProgress: Long = 0,
) : UiState