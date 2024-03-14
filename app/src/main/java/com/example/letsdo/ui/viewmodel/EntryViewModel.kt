package com.example.letsdo.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letsdo.data.EntriesRepository
import com.example.letsdo.data.Entry
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EntryScreenViewModel(
    private val entriesRepository: EntriesRepository
): ViewModel() {

    private val entryId: Int = 1

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }

    var uiState by mutableStateOf(Entry(title ="", description = ""))
        private set


//    val uiState: StateFlow<EntryUiState> =
//        entriesRepository.getEntryStream(entryId).filterNotNull().map {
//            EntryUiState(entryDetails = it)
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//            initialValue = EntryUiState()
//        )


    fun addEntry() {
        viewModelScope.launch {
            entriesRepository.insertEntry(uiState)
        }

    }

    fun validateInput(): Boolean {
        return uiState.description.isNotBlank()
    }

    fun updateUiStateTitle(it: String){
        uiState = uiState.copy(title = it)
    }

    fun updateUistateDesc(it: String) {
        uiState = uiState.copy(description = it)
    }

}

//
//data class EntryUiState(
//    val id: Int = 0,
//    val title: String = "",
//    val description: String = "",
//    val marked: Boolean = false
//)