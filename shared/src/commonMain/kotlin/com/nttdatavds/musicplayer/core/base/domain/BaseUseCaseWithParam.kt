package com.nttdatavds.musicplayer.core.base.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseUseCaseWithParam<PARAM, RESULT>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    abstract suspend fun executeBlock(param: PARAM): RESULT
    suspend operator fun invoke(param: PARAM): Result<RESULT> = withContext(dispatcher) {
        try {
            Result.success(executeBlock(param))
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

}