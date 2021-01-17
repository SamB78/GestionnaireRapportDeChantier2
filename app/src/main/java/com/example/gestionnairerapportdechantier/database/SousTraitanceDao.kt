package com.example.gestionnairerapportdechantier.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestionnairerapportdechantier.entities.SousTraitance

@Dao
interface SousTraitanceDao {
    @Insert
    fun insertSousTraitance(sousTraitance: SousTraitance): Long

    @Update
    fun updateSousTraitance(sousTraitance: SousTraitance)

    @Query("SELECT * FROM sous_traitance")
    fun getAllFromSousTraitance(): List<SousTraitance>

    @Query("SELECT * FROM sous_traitance")
    fun getAllFromSousTraitanceLiveData(): LiveData<List<SousTraitance>>


    @Query("SELECT * FROM sous_traitance WHERE id  = :id")
    fun getSousTraitanceById(id: Long): SousTraitance

    @Query("SELECT * FROM sous_traitance WHERE id  IN (:listIds)")
    fun getSousTraitanceByIds(listIds: List<Int>): List<SousTraitance>
}