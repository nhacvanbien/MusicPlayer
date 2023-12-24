package com.nttdatavds.musicplayer.presentation.screens.playlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nttdatavds.musicplayer.domain.model.Audio

@Composable
fun PlayList(
    modifier: Modifier,
    audios: List<Audio>,
    playingAudioUri: String? = null,
    onAudioItemClick: (Audio) -> Unit,
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        state = listState
    ) {
        itemsIndexed(audios) { index, audio ->
            AudioListItem(
                modifier = Modifier.fillMaxWidth(),
                index = index + 1,
                audio = audio,
                isPlaying = playingAudioUri == audio.uri,
                onAudioClick = {
                    onAudioItemClick(audio)
                }
            )
        }
    }
    LaunchedEffect(playingAudioUri) {
        val playingIndex = audios.indexOfFirst { it.uri == playingAudioUri }
        if (playingIndex > 0) {
            listState.animateScrollToItem(playingIndex)
        }
    }
}
