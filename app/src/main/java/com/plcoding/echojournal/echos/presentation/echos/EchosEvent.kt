package com.plcoding.echojournal.echos.presentation.echos

sealed interface EchosEvent{
    data object RequestAudioPermission: EchosEvent
}