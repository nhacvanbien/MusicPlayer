package com.nttdatavds.musicplayer.android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nttdatavds.musicplayer.Greeting
import com.nttdatavds.musicplayer.MusicPlayerApp

class MainActivity : ComponentActivity() {
    private val permissionsToRequest = arrayOf(
        if (Build.VERSION.SDK_INT >= 33)
            "android.permission.READ_MEDIA_AUDIO"
        else
            "android.permission.READ_EXTERNAL_STORAGE"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {

                val visiblePermissionDialogQueue = remember {
                    mutableStateListOf<String>()
                }
                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        permissionsToRequest.forEach { permission ->
                            val isGranted = perms[permission] == true
                            if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
                                visiblePermissionDialogQueue.add(permission)
                            }
                        }
                    }
                )

                val lifeCycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifeCycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            multiplePermissionResultLauncher.launch(permissionsToRequest)
                        }
                    }
                    lifeCycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifeCycleOwner.lifecycle.removeObserver(observer)
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MusicPlayerApp()
                }
//                if (visiblePermissionDialogQueue.size > 0) {
//                    visiblePermissionDialogQueue
//                        .reversed()
//                        .forEach { permission ->
//                            PermissionDialog(
//                                permissionTextProvider = when (permission) {
//                                    permissionsToRequest.first() -> {
//                                        ReadAudioPermissionTextProvider()
//                                    }
//
//                                    else -> return@forEach
//                                },
//                                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
//                                    permission
//                                ),
//                                onDismiss = {
//                                    finish()
//                                },
//                                onOkClick = {
//                                    visiblePermissionDialogQueue.removeFirst()
//                                    multiplePermissionResultLauncher.launch(
//                                        arrayOf(permission)
//                                    )
//                                },
//                                onGoToAppSettingsClick = ::openAppSettings
//                            )
//                        }
//                }
            }
        }
    }
}
fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

