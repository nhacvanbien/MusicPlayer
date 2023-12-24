package com.nttdatavds.musicplayer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.nttdatavds.musicplayer.presentation.screens.player.MediaPlayerScreen
import com.nttdatavds.musicplayer.presentation.screens.playlist.favoriteplaylist.FavoritePlayListScreen
import com.nttdatavds.musicplayer.presentation.screens.playlist.localplaylist.LocalPlayListScreen
import com.nttdatavds.musicplayer.presentation.screens.playlist.remoteplaylist.RemotePlayListScreen
import com.nttdatavds.musicplayer.presentation.ui.theme.Black
import com.nttdatavds.musicplayer.presentation.ui.theme.bottomBarHeight

@Composable
fun MusicPlayerApp(
) {
    TabNavigator(
        RemotePlayListScreen
    ) { tabNavigator ->
        Scaffold(
            content = {
                Box(
                    modifier = Modifier.padding(bottom = bottomBarHeight),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    MediaPlayerScreen.Content()
                }
            },
            bottomBar = {
                BottomNavigation(backgroundColor = Black) {
                    TabNavigationItem(RemotePlayListScreen)
                    TabNavigationItem(LocalPlayListScreen)
                    TabNavigationItem(FavoritePlayListScreen)
                }
            }
        )
    }
}

@Composable
fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current.key == tab.key,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                painter = tab.options.icon!!,
                contentDescription = tab.options.title,
            )
        },
        label = {
                Text(tab.options.title)
        },
        selectedContentColor = Color(0xff8130bc),
        unselectedContentColor = Color.White,
    )
}
