package com.example.letsdo.data

import kotlinx.coroutines.flow.Flow


class OfflineEntriesRepository(private val entryDao: EntryDao): EntriesRepository  {
    override suspend fun insertEntry(entry: Entry) = entryDao.insert(entry)

    override suspend fun deleteEntry(entry: Entry) = entryDao.delete(entry)

    override fun getAllEntriesStream(): Flow<List<Entry>> = entryDao.getAllEntries()

    override suspend fun getEntryStream(id: Int): Entry = entryDao.getEntry(id)

    override fun getEntriesStream(search: String): Flow<List<Entry>> = entryDao.getEntries(search)

    override suspend fun updateEntry(entry: Entry)  = entryDao.update(entry)
}