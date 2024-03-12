package com.example.letsdo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface EntryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    @Delete
    suspend fun delete(entry: Entry)

    @Query("SELECT * FROM entry ORDER BY id DESC")
    fun getAllEntries(): Flow<List<Entry>>

    @Query("SELECT * FROM entry WHERE LOWER(title) LIKE LOWER(:search) OR LOWER(description) LIKE LOWER(:search)")
    fun getEntries(search: String): Flow<List<Entry>>

    @Query("SELECT * FROM entry WHERE id = :id")
    fun getEntry(id: Int): Flow<Entry>

}