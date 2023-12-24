package com.nttdatavds.musicplayer.core.base.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> ResourceUIContainer(
    modifier: Modifier = Modifier,
    resourceUiState: ResourceUiState<T>,
    successView: @Composable (data: T) -> Unit,
    loadingView: @Composable () -> Unit = { LoadingView() },
    onTryAgainWhenError: () -> Unit,
    errorMsg: String = "There was an error occurred",
    onCheckAgainWhenEmpty: () -> Unit,
    emptyMsg: String = "There is no data to display"
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        when (resourceUiState) {
            ResourceUiState.Init -> Unit
            ResourceUiState.Empty -> EmptyView(
                modifier = modifier,
                onCheckAgainWhenEmpty = onCheckAgainWhenEmpty,
                emptyMsg = emptyMsg
            )

            is ResourceUiState.Error -> ErrorView(
                modifier = modifier,
                onTryAgain = onTryAgainWhenError,
                errorMsg = errorMsg
            )

            ResourceUiState.Loading -> loadingView()
            is ResourceUiState.Success -> successView(resourceUiState.data)
        }
    }
}