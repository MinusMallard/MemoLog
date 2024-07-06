package com.example.letsdo.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letsdo.data.EntriesRepository
import com.example.letsdo.data.Entry
import kotlinx.coroutines.launch

class EntryScreenViewModel(
    private val entriesRepository: EntriesRepository
): ViewModel() {

    var uiState by mutableStateOf(Entry(title ="", description = ""))
        private set

    fun addEntry() {
        viewModelScope.launch {
            if (uiState.id == -1) {
                entriesRepository.insertEntry(Entry(title = uiState.title, description = uiState.description))
            } else {
                entriesRepository.updateEntry(Entry(id = uiState.id, title = uiState.title, description = uiState.description))
            }
        }
    }

    fun validateInput(): Boolean {
        return uiState.description.isNotBlank()
    }

    fun updateUiStateTitle(it: String){
        uiState = uiState.copy(title = it)
    }

    fun updateUiStateDesc(it: String) {
        uiState = uiState.copy(description = it)
    }

    fun updateUiStateId(it: Int) {
        uiState = uiState.copy(id = it)
    }

    fun loadEntry(id: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(id = entriesRepository.getEntryStream(id).id ,title = entriesRepository.getEntryStream(id).title, description = entriesRepository.getEntryStream(id).description);
        }
    }

}