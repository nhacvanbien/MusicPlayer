package com.nttdatavds.musicplayer.presentation.screens.playlist.favoriteplaylist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object FavoritePlayListScreen : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.FavoriteBorder)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Favorite",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        LifecycleEffect(
            onStarted = { },
            onDisposed = { }
        )
    }
}
