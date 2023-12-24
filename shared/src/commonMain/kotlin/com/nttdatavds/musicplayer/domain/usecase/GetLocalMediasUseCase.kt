package com.nttdatavds.musicplayer.domain.usecase

import com.nttdatavds.musicplayer.core.base.domain.BaseUseCaseWithoutParam
import com.nttdatavds.musicplayer.domain.model.Audio
import com.nttdatavds.musicplayer.domain.repository.MediaRepository
import kotlinx.coroutines.CoroutineDispatcher

class GetLocalMediasUseCase(
    private val mediaRepository: MediaRepository,
    dispatcher: CoroutineDispatcher
) : BaseUseCaseWithoutParam<List<Audio>>(dispatcher) {
    override suspend fun executeBlock(): List<Audio> = mediaRepository.getLocalAudios()
}