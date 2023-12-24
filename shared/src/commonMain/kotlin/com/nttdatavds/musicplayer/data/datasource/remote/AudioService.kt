package com.nttdatavds.musicplayer.data.datasource.remote

import com.nttdatavds.musicplayer.data.datasource.remote.AudioApiResult
import com.nttdatavds.musicplayer.data.datasource.remote.AudioDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class AudioService(private val ktorClient: HttpClient) {
    suspend fun getAudios(): List<AudioDto>? {
        return ktorClient.get("$END_POINT/v1/records/28cb544d-2154-41a2-9e72-229344321315")
            .body<AudioApiResult>()
            .data.data
    }
    companion object {
        private const val END_POINT = "https://api.myjson.online"
    }
}