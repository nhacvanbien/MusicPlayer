package com.nttdatavds.musicplayer.core.base.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseUseCaseWithoutParam<RESULT>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    abstract suspend fun executeBlock(): RESULT
    suspend operator fun invoke(): Result<RESULT> = withContext(dispatcher) {
        try {
            Result.success(executeBlock())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

}