package com.plcoding.echojournal.core.presentation.util

import java.util.Locale
import kotlin.time.Duration
import kotlin.time.DurationUnit

fun Duration.formatMMSS(): String{
     val totalSeconds = this.toLong(DurationUnit.SECONDS)
     val minutes = totalSeconds/60
     val seconds = totalSeconds % 60
     return String.format(
         locale = Locale.getDefault(),
         "%2d:%2d",
         minutes,
         seconds
     )
}