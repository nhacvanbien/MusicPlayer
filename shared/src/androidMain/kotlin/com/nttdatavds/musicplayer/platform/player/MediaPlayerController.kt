package com.nttdatavds.musicplayer.platform.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import com.nttdatavds.musicplayer.services.MediaPlayerService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

actual class MediaPlayerController(val applicationContext: Context) {

    actual val audioStates: MutableStateFlow<AudioState> = MutableStateFlow(AudioState.Initial)

    private var musicService: MediaPlayerService? = null
    private var isBound = false

    private var exoPlayer: ExoPlayer? = null

    private var job: Job? = null

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updatePlayingStateChange(isPlaying)
        }

        override fun onTracksChanged(tracks: Tracks) {
            updatePlayingTrack()
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.ServiceBinder
            musicService = binder.getService()
            isBound = true
            exoPlayer = musicService?.exoPlayer
            exoPlayer?.addListener(playerListener)
            updateCurrentPlayingStateForTheFirstTime()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            musicService = null
            exoPlayer?.removeListener(playerListener)
        }
    }

    private fun updateCurrentPlayingStateForTheFirstTime() {
        exoPlayer?.let {
            updatePlayingTrack()
            GlobalScope.launch(Dispatchers.Main) {
                startUpdateProgress()
            }
        }
    }

    init {
        startService()
    }

    private fun startService() {
        val intent = Intent(applicationContext, MediaPlayerService::class.java)
        applicationContext.startService(intent)
        applicationContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun unBindService() {
        if (isBound) {
            applicationContext.unbindService(connection)
        }
    }

    actual fun onMediaControlEvent(controlEvent: MediaControlEvent) {
        when (controlEvent) {
            is MediaControlEvent.StartEvent -> {
                val position = controlEvent.audios.indexOf(controlEvent.audio)
                val mediaItems = controlEvent.audios.map {
                    val mediaMetaData = MediaMetadata.Builder()
                        .setTitle(it.title)
                        .setArtworkUri(Uri.parse(it.artworkUri))
                        .build()
                    MediaItem.Builder()
                        .setUri(it.uri)
                        .setMediaMetadata(mediaMetaData)
                        .build()
                }
                musicService?.start(position, mediaItems)
            }

            MediaControlEvent.PlayOrPauseEvent -> {
                musicService?.playOrPause()
            }

            is MediaControlEvent.SeekToEvent -> {
                musicService?.seekTo(controlEvent.position)

            }

            MediaControlEvent.SeekToNextEvent -> {
                musicService?.seekToNextMedia()
            }

            MediaControlEvent.SeekToPreviousEvent -> {
                musicService?.seekToPreviousMedia()
            }

            MediaControlEvent.StopEvent -> {
                unBindService()
            }
        }
    }

    private fun updatePlayingTrack() {
        val uri = exoPlayer?.currentMediaItem?.localConfiguration?.uri
        uri?.let {
            audioStates.value =
                AudioState.CurrentAudioChange(it.toString())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun updatePlayingStateChange(isPlaying: Boolean) {
        audioStates.value = AudioState.MediaPlaying(isPlaying)
        if (isPlaying) {
            GlobalScope.launch(Dispatchers.Main) {
                startUpdateProgress()
            }
        } else {
            stopUpdateProgress()
        }
    }

    private suspend fun startUpdateProgress() = job.run {
        while (true) {
            delay(100)
            exoPlayer?.let { audioStates.value = AudioState.ProgressChanged(it.currentPosition) }
        }
    }

    private fun stopUpdateProgress() {
        job?.cancel()
    }
}