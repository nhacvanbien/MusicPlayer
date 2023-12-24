package com.nttdatavds.musicplayer.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.NotificationUtil.IMPORTANCE_LOW
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerNotificationManager
import com.nttdatavds.musicplayer.R
import com.nttdatavds.musicplayer.core.common.Constant


class MediaPlayerService : Service() {

    private val serviceBinder: IBinder = ServiceBinder()

    inner class ServiceBinder : Binder() {
        fun getService(): MediaPlayerService {
            return this@MediaPlayerService
        }
    }

    var exoPlayer: ExoPlayer? = null
        private set

    private var notificationManager: PlayerNotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        initExoPlayer()
        initNotificationManager()
    }

    private fun initExoPlayer() {
        //assign variables
        exoPlayer = ExoPlayer.Builder(applicationContext).build()

        //Audio focus attributes
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        exoPlayer?.setAudioAttributes(audioAttributes, true)
    }

    @OptIn(UnstableApi::class)
    private fun initNotificationManager() {
        //notification manager
        val channelId = Constant.MUSIC_CHANNEL
        val notificationId = Constant.NOTIFICATION_ID

        notificationManager =
            PlayerNotificationManager.Builder(this, notificationId, channelId)
                .setChannelImportance(IMPORTANCE_LOW)
                .setChannelNameResourceId(R.string.app_name)
                .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
                    override fun onNotificationCancelled(
                        notificationId: Int,
                        dismissedByUser: Boolean
                    ) {
                        super.onNotificationCancelled(notificationId, dismissedByUser)
                        stopSelf()
                    }

                    override fun onNotificationPosted(
                        notificationId: Int,
                        notification: Notification,
                        ongoing: Boolean
                    ) {
                        super.onNotificationPosted(notificationId, notification, ongoing)
                        if (ongoing) {
                            startForeground(notificationId, notification);
                        } else {
                            stopForeground(false)
                        }
                    }
                })
                .build()

        notificationManager?.run {
            setPlayer(exoPlayer)
            setPriority(NotificationCompat.PRIORITY_MAX)
            setUseRewindAction(false)
            setUseFastForwardAction(false)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        return serviceBinder
    }

    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        exoPlayer?.release()
        notificationManager?.setPlayer(null)
        exoPlayer = null
    }

    fun start(position: Int, mediaItems: List<MediaItem>) {
        exoPlayer?.setMediaItems(mediaItems, position, 0)
        exoPlayer?.playWhenReady = true
        exoPlayer?.prepare()
        exoPlayer?.play()
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
    }

    fun playOrPause() {
        exoPlayer?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    fun seekToPreviousMedia() {
        exoPlayer?.seekToPreviousMediaItem()
    }

    fun seekToNextMedia() {
        exoPlayer?.seekToNextMediaItem()
    }
}