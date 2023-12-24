package com.nttdatavds.musicplayer.platform.player

import com.nttdatavds.musicplayer.domain.model.Audio
import kotlinx.coroutines.flow.MutableStateFlow

sealed class MediaControlEvent {
    data class StartEvent(val audio: Audio, val audios: List<Audio>) : MediaControlEvent()
    data object PlayOrPauseEvent : MediaControlEvent()
    data object SeekToNextEvent : MediaControlEvent()
    data object SeekToPreviousEvent : MediaControlEvent()
    data class SeekToEvent(val position: Long) : MediaControlEvent()
    data object StopEvent : MediaControlEvent()
}

sealed class AudioState {
    data object Initial : AudioState()
    data class ProgressChanged(val progress: Long) : AudioState()
    data class MediaPlaying(val isPlaying: Boolean) : AudioState()
    data class CurrentAudioChange(val uri: String) : AudioState()
}

expect class MediaPlayerController {
    val audioStates: MutableStateFlow<AudioState>
    fun  onMediaControlEvent(controlEvent: MediaControlEvent)
}