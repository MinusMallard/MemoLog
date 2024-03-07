package com.example.letsdo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room

import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi


@Database(entities = [Entry::class],version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun entryDao(): EntryDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): AppDatabase {
            return Instance?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java,"entry_database")
                .build()
                .also { Instance = it}
            }

        }
    }
}