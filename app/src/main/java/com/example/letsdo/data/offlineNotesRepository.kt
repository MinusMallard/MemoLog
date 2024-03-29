package com.example.letsdo.data

import kotlinx.coroutines.flow.Flow

class offlineNotesRepository(private val noteDao: NoteDao): NotesRepository {
    override suspend fun insertNote(entry: Note) = noteDao.insert(entry)
    override suspend fun updateNote(entry: Note)  = noteDao.update(entry)

    override suspend fun deleteNote(entry: Note)  = noteDao.delete(entry)

    override fun getNotesStream(search: String): Flow<List<Note>>  = noteDao.getNotes(search)

    override fun getAllNoteStream(): Flow<List<Note>> = noteDao.getAllNotes()

    override suspend fun getNote(id: Int): Note = noteDao.getNote(id)
}