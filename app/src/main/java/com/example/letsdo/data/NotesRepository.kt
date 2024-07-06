package com.example.letsdo.data

import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun insertNote(entry: Note)

    suspend fun updateNote(entry: Note)

    suspend fun deleteNote(entry: Note)

    fun getNotesStream(search: String): Flow<List<Note>>

    fun getAllNoteStream(): Flow<List<Note>>

    suspend fun getNote(id: Int): Note
}