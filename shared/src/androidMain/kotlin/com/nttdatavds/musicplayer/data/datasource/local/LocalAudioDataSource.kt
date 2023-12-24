package com.nttdatavds.musicplayer.data.datasource.local

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

actual class LocalAudioDataSource(val appContext: Context) {

    private val ALBUM_ART = "content://media/external/audio/albumart"

    actual suspend fun getAllAudioFiles(): List<LocalAudio> {
        return getAllMedias(appContext)
    }

    @SuppressLint("Range")
    fun getAllMedias(appContext: Context): List<LocalAudio> {
        val audioFiles = mutableListOf<LocalAudio>()
        val projections =
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST
            )

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val cursor = appContext.contentResolver.query(
            collection,
            projections,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndex(MediaStore.Audio.Media._ID))
                var name = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val duration = it.getInt(it.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val size = it.getInt(it.getColumnIndex(MediaStore.Audio.Media.SIZE))
                val albumId = it.getLong(it.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                val albumArtworkUri = ContentUris.withAppendedId(
                    Uri.parse(ALBUM_ART),
                    albumId
                )
                name = name.substring(0, name.lastIndexOf("."))

                audioFiles.add(
                    LocalAudio(
                        name,
                        uri.toString(),
                        albumArtworkUri.toString(),
                        size,
                        duration
                    )
                )
            }
        }
        return audioFiles
    }
}
