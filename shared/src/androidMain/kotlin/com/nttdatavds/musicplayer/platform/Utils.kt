package com.nttdatavds.musicplayer.platform

actual fun kmmStringFormat(format: String, vararg params: Any?): String {
    return String.Companion.format(format, *params)
}