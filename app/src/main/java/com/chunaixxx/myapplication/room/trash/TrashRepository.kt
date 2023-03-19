package com.chunaixxx.myapplication.room.trash

import androidx.lifecycle.LiveData

class TrashRepository(private val trashDao: TrashDao) {

    fun getAllNotes() : LiveData<List<Trash>> = trashDao.getAllNotes()

    suspend fun insertNote(trash: Trash){
        trashDao.insert(trash)
    }

    suspend fun deleteNote(trash: Trash){
        trashDao.delete(trash)
    }

    fun deleteAllNotes(){
        trashDao.deleteAllNotes()
    }

}