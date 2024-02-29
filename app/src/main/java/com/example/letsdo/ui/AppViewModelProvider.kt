package com.example.letsdo.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.letsdo.ToDoApplication
import com.example.letsdo.ui.viewmodel.EntryScreenViewModel
import com.example.letsdo.ui.viewmodel.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                this.toDoApplication().container.entriesRepository
            )
        }
        initializer {
            EntryScreenViewModel(
                this.toDoApplication().container.entriesRepository
            )
        }
    }
}


fun CreationExtras.toDoApplication(): ToDoApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ToDoApplication)