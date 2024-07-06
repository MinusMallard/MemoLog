package com.example.letsdo.ui.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.letsdo.data.AppDatabase
import com.example.letsdo.data.Entry
import com.example.letsdo.data.Note
import com.example.letsdo.data.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(
    private var appDatabase: NotesRepository
): ViewModel() {
    val noteUiState: StateFlow<NoteScreenUiState> = appDatabase.getAllNoteStream().map { NoteScreenUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NoteScreenUiState()
        )

    fun updateNote(note: Note) {
        viewModelScope.launch {
            appDatabase.updateNote(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        appDatabase.deleteNote(note)
    }

    var uiState by mutableStateOf(Note(title = "", description = ""))
        private set

    fun addNote() {
        viewModelScope.launch {
            appDatabase.insertNote(uiState)
        }
    }

    fun validateInput(): Boolean {
        return uiState.description.isNotBlank()
    }
    fun updateUiStateTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun updateUiStateDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    fun loadNote(id: Int) {
        viewModelScope.launch {
            uiState = appDatabase.getNote(id);
        }
    }
}

data class NoteScreenUiState(val noteList: List<Note> = listOf())