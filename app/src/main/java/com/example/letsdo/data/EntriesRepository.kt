package com.example.letsdo.data

import kotlinx.coroutines.flow.Flow


interface EntriesRepository {

    suspend fun insertEntry(entry: Entry)

    suspend fun updateEntry(entry: Entry)

    suspend fun deleteEntry(entry: Entry)

    fun getEntriesStream(search: String): Flow<List<Entry>>

    fun getAllEntriesStream(): Flow<List<Entry>>

    fun getEntryStream(id: Int): Flow<Entry>
}