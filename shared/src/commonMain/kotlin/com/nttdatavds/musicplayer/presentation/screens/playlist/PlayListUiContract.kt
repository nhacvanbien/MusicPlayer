package com.nttdatavds.musicplayer.presentation.screens.playlist

import com.nttdatavds.musicplayer.core.base.presentation.ui.ResourceUiState
import com.nttdatavds.musicplayer.core.base.presentation.viewmodel.UiEvent
import com.nttdatavds.musicplayer.core.base.presentation.viewmodel.UiState
import com.nttdatavds.musicplayer.domain.model.Audio

data class PlayListUiState(
    val audios: ResourceUiState<List<Audio>>,
    val playingAudioUri: String? = null
) : UiState

sealed interface PlayListUiEvent : UiEvent {
    data object TryToLoadAudiosAgain : PlayListUiEvent
}
