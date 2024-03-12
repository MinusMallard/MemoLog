package com.example.letsdo.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "entry")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    var marked: Boolean = false,
)