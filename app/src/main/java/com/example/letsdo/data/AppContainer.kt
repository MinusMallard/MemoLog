package com.example.letsdo.data

import android.content.Context

interface AppContainer {
    val entriesRepository: EntriesRepository
}

class AppDataContainer(private val context: Context): AppContainer {


    override val entriesRepository: EntriesRepository by lazy {
        OffllineEntriesRepository(AppDatabase.getDatabase(context).entryDao())
    }
}