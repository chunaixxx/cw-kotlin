package com.chunaixxx.myapplication.room.trash

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TrashDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trash: Trash)

    @Delete
    suspend fun delete(trash: Trash)

    @Query("select * from trash_table order by id ASC")
    fun getAllNotes() : LiveData<List<Trash>>

    @Query("delete from trash_table")
    fun deleteAllNotes()
}