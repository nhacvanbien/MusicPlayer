package com.nttdatavds.musicplayer.core.base.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<S : UiState, T : UiEvent> : ScreenModel {
    private val initialState: S by lazy { initState() }

    protected val _uiState: MutableStateFlow<S> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    abstract fun handleUiEvent(event: T)
    abstract fun initState(): S
}