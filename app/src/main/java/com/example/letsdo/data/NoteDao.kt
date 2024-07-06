package com.example.letsdo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM note ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE LOWER(title) LIKE LOWER(:search) OR LOWER(description) LIKE LOWER(:search)")
    fun getNotes(search: String): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id IS(:id)")
    suspend fun getNote(id: Int): Note

}