package com.plcoding.echojournal.echos.presentation.echos

import com.plcoding.echojournal.echos.presentation.echos.models.EchoFilterChip

sealed interface EchosAction {
    data object OnMoodChipClick: EchosAction
    data object OnTopicChipClick: EchosAction
    data object OnFabClick: EchosAction
    data object OnSettingsClick: EchosAction
    data object OnFABLongClick: EchosAction
    data class OnRemoveFilters(val filterType: EchoFilterChip): EchosAction
}