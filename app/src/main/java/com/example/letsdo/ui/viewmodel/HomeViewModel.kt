package com.example.letsdo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letsdo.data.EntriesRepository
import com.example.letsdo.data.Entry
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class HomeViewModel(
    private val entriesRepository: EntriesRepository
): ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }

    val homeUiState: StateFlow<HomeUiState> = entriesRepository.getAllEntriesStream().map { HomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    suspend fun updateEntry(entry: Entry) {
        entriesRepository.updateEntry(entry)
    }

    suspend fun deleteEntry(entry: Entry) {
        entriesRepository.deleteEntry(entry)
    }

//    suspend fun searchEntry(search: String): StateFlow<HomeUiState> {
//        val searchUiState: StateFlow<HomeUiState> = entriesRepository.getEntriesStream(search).map { HomeUiState(it) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = HomeUiState()
//            )
//
//        return searchUiState
//    }
}

data class HomeUiState(val entryList: List<Entry> = listOf())

