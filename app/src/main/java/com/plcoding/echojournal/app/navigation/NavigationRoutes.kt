package com.plcoding.echojournal.app.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationRoutes {

    @Serializable
    data class Echos(
        val startRecording: Boolean
    ): NavigationRoutes

    @Serializable
    data class CreateEcho(
        val recordingPath:String,
        val duration:Long,
        val amplitudes:String
    ): NavigationRoutes

    @Serializable
    data object Settings
}