package com.example.gestionnairerapportdechantier.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestionnairerapportdechantier.entities.Materiaux

@Dao
interface MateriauxDao {

    @Insert
    fun insertMateriaux(materiaux: Materiaux): Long

    @Update
    fun updateMateriaux(materiaux: Materiaux)

    @Query("SELECT * FROM materiaux")
    fun getAllFromMateriaux(): List<Materiaux>

    @Query("SELECT * FROM materiaux")
    fun getAllFromMateriauxLiveData(): LiveData<List<Materiaux>>


    @Query("SELECT * FROM materiaux WHERE id  = :id")
    fun getMateriauxById(id: Long): Materiaux

    @Query("SELECT * FROM materiaux WHERE id  IN (:listIds)")
    fun getMateriauxByIds(listIds: List<Int>): List<Materiaux>
}