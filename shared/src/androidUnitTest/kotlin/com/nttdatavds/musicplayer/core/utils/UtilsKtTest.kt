package com.nttdatavds.musicplayer.core.utils


import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsKtTest {
    @Test
    fun testZeroMillis() {
        val result = durationToMinutesSeconds(0)
        assertEquals<Pair<Long, Long>>(Pair(0, 0), result)
    }

    @Test
    fun testLessThanOneMinute() {
        val result = durationToMinutesSeconds(5000)
        assertEquals<Pair<Long, Long>>(Pair(0, 5), result)
    }

    @Test
    fun testOneMinute() {
        val result = durationToMinutesSeconds(60_000)
        assertEquals<Pair<Long, Long>>(Pair(1, 0), result)
    }

    @Test
    fun testMoreThanOneMinute() {
        val result = durationToMinutesSeconds(123_456)
        assertEquals<Pair<Long, Long>>(Pair(2, 3), result)
    }

    @Test
    fun testBoundaryMinute() {
        val result = durationToMinutesSeconds(59_999)
        assertEquals<Pair<Long, Long>>(Pair(0, 59), result)
    }

    @Test
    fun testBoundarySecond() {
        val result = durationToMinutesSeconds(60_999)
        assertEquals<Pair<Long, Long>>(Pair(1, 0), result)
    }

    @Test
    fun testLargeDuration() {
        val result = durationToMinutesSeconds(3600_000)
        assertEquals<Pair<Long, Long>>(Pair(60, 0), result)
    }
}