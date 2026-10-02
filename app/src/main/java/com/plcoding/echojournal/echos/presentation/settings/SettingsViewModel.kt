@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.plcoding.echojournal.echos.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.echojournal.echos.domain.echo.EchoDataSource
import com.plcoding.echojournal.echos.domain.echo.Mood
import com.plcoding.echojournal.echos.domain.settings.SettingsPreferences
import com.plcoding.echojournal.echos.presentation.models.MoodUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SettingsViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val echoDataSource: EchoDataSource
) : ViewModel() {
    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeSettings()
                observeTopicSearchResults()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SettingsState()
        )

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnAddButtonClick -> onAddTopicClick()
            is SettingsAction.OnSelectTopicClick -> onSelectTopicClick(action.topic)
            SettingsAction.OnDismissTopicDropdown -> onDismissTopicDropdown()
            is SettingsAction.OnMoodClick -> onMoodClick(action.mood)
            is SettingsAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            is SettingsAction.OnSearchTextChange -> onSearchTextChange(action.text)
            else -> Unit
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeTopicSearchResults() {
        state
            .distinctUntilChangedBy{it.searchText}
            .map { it.searchText }
            .debounce(300.milliseconds)
            .flatMapLatest {
                query->
                if(query.isNotBlank()){
                    echoDataSource.searchTopics(query)
                }
                else emptyFlow()
            }
            .onEach {
                filterResults->
                _state.update {
                    val filteredNonDefaultResult = filterResults - it.topics.toSet()
                    val searchText = it.searchText.trim()
                    val isNewTopic = searchText !in filteredNonDefaultResult && searchText !in it.topics
                            && searchText.isNotBlank()
                    it.copy(
                        suggestedTopics = filteredNonDefaultResult,
                        isTopicSuggestionsVisible = filterResults.isNotEmpty() || isNewTopic,
                        showCreateTopicOption = isNewTopic
                    )
                }
            }
            .launchIn(viewModelScope)
    }
    private fun onSearchTextChange(text: String) {
        _state.update {
            it.copy(searchText = text)
        }
    }

    private fun onDismissTopicDropdown() {
        _state.update {
            it.copy(
                isTopicSuggestionsVisible = true
            )
        }
    }

    private fun onAddTopicClick() {
        _state.update {
            it.copy(
                isTopicTextInputVisible = true
            )
        }
    }

    private fun onMoodClick(mood: MoodUi) {
        viewModelScope.launch {
            settingsPreferences.saveDefaultMood(Mood.valueOf(mood.name))
        }
    }

    private fun onSelectTopicClick(topic: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isTopicTextInputVisible = false,
                    isTopicSuggestionsVisible = false,
                    searchText = ""
                )
            }
            val newTopics = (state.value.topics+topic).distinct()
            settingsPreferences.saveDefaultTopics(newTopics)
        }
    }

    private fun onRemoveTopicClick(topic: String) {
        viewModelScope.launch {
            val newTopics = (state.value.topics-topic).distinct()
            settingsPreferences.saveDefaultTopics(newTopics)
        }
    }

    private fun observeSettings() {
        combine(
            settingsPreferences.observeDefaultTopics(),
            settingsPreferences.observeDefaultMood()
        ){ topics,mood->
            _state.update { it.copy(
                topics = topics,
                selectedMood = MoodUi.valueOf(mood.name)
            ) }
        }.launchIn(viewModelScope)
    }

}