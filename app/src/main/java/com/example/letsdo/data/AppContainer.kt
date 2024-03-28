package com.example.letsdo.data

import android.content.Context

interface AppContainer {
    val entriesRepository: EntriesRepository
    val notesRepository: NotesRepository
}

class AppDataContainer(private val context: Context): AppContainer {


    override val entriesRepository: EntriesRepository by lazy {
        OfflineEntriesRepository(AppDatabase.getDatabase(context).entryDao())
    }

    override val notesRepository: NotesRepository by lazy {
        offlineNotesRepository(AppDatabase.getDatabase(context).noteDao())
    }
}