package com.chunaixxx.myapplication.room.note

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("select * from notes_table order by id ASC")
    fun getAllNotes() : LiveData<List<Note>>

    @Update
    suspend fun update(note: Note)
}