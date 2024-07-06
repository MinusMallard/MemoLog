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
            entriesRepository.insertEntry(uiState)
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

    fun loadEntry(id: Int) {
        viewModelScope.launch {
            uiState = entriesRepository.getEntryStream(id);
        }
    }

}