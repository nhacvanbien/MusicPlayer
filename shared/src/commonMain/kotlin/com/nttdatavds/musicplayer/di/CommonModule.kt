package com.nttdatavds.musicplayer.di

import com.nttdatavds.musicplayer.data.datasource.remote.AudioService
import com.nttdatavds.musicplayer.data.datasource.remote.RemoteDataSource
import com.nttdatavds.musicplayer.data.datasource.remote.RemoteDataSourceImpl
import com.nttdatavds.musicplayer.data.repository.MediaRepositoryImpl
import com.nttdatavds.musicplayer.domain.repository.MediaRepository
import com.nttdatavds.musicplayer.domain.usecase.GetLocalMediasUseCase
import com.nttdatavds.musicplayer.domain.usecase.GetRemoteMediasUseCase
import com.nttdatavds.musicplayer.presentation.screens.player.PlayerViewModel
import com.nttdatavds.musicplayer.presentation.screens.playlist.localplaylist.LocalPlayListViewModel
import com.nttdatavds.musicplayer.presentation.screens.playlist.remoteplaylist.RemotePlayListViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

val ktorModule = module {
    single<AudioService> { AudioService(ktorClient = get()) }
    single<HttpClient> {
        HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
        }
    }
}

val dispatcherModule = module {
    factory { Dispatchers.Default }
}

val viewModelModule = module {
    single<LocalPlayListViewModel> {
        LocalPlayListViewModel(
            getLocalMediasUseCase = get(),
            playerController = get()
        )
    }

    single<RemotePlayListViewModel> {
        RemotePlayListViewModel(
            getRemoteMediasUseCase = get(),
            playerController = get()
        )
    }

    single<PlayerViewModel> {
        PlayerViewModel(
            playerController = get()
        )
    }
}

val useCasesModule: Module = module {
    factory<GetLocalMediasUseCase> {
        GetLocalMediasUseCase(
            dispatcher = get(),
            mediaRepository = get()
        )
    }
    factory<GetRemoteMediasUseCase> {
        GetRemoteMediasUseCase(
            dispatcher = get(),
            mediaRepository = get()
        )
    }
}

val repositoryModule = module {
    single<MediaRepository> {
        MediaRepositoryImpl(
            localMediaDataSource = get(),
            remoteDataSource = get()
        )
    }

    single<RemoteDataSource> { RemoteDataSourceImpl(get()) }
}
