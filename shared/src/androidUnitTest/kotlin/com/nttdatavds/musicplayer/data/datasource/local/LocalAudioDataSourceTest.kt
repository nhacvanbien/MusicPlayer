package com.nttdatavds.musicplayer.data.datasource.local

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LocalAudioDataSourceTest {
    @Test
    fun getAllAudioFiles_returnsListOfLocalAudio() = runBlocking{
        val mockContext = mockk<Context>()
        val dataSource = LocalAudioDataSource(mockContext)

        val currsor = mockk<Cursor>()
        every { mockContext.contentResolver.query(any(), any(), any(), any(), any()) } returns currsor

        every { currsor.moveToNext() } returns false
        every { currsor.close() } just Runs
        val audioFiles = dataSource.getAllAudioFiles()

        assertNotNull(audioFiles)
        assertEquals(audioFiles.size, 0)
    }

    @Test
    fun getAllMedias_returnsCorrectAudioData() = runBlocking {
        val mockCursor = mockk<Cursor>(relaxed = true)
        every { mockCursor.moveToNext() } returnsMany listOf(true, false)
        every { mockCursor.getLong(any()) } returns 1L
        every { mockCursor.getString(any()) } returns "name.mp3"
        every { mockCursor.getInt(any()) } returnsMany listOf(1024, 30000)

        val mockContext = mockk<Context>()
        every { mockContext.contentResolver.query(any(), any(), any(), any(), any()) } returns mockCursor
        val uri = mockk<Uri>()
        val albumArt = mockk<Uri>()
        mockkStatic("android.net.Uri")
        every { Uri.parse(any()) } returns mockk<Uri>()

        mockkStatic("android.content.ContentUris")
        every { ContentUris.withAppendedId(any(), any()) } returnsMany listOf(uri, albumArt)

        val dataSource = LocalAudioDataSource(mockContext)
        val audioFiles = dataSource.getAllMedias(mockContext)

        val expectedAudio = LocalAudio("name", uri.toString(), albumArt.toString(),30000, 1024)
        assertContains(audioFiles, expectedAudio)
    }

}