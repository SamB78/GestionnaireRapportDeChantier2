package com.example.gestionnairerapportdechantier.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestionnairerapportdechantier.entities.Materiel
import com.example.gestionnairerapportdechantier.entities.MaterielLocation

@Dao
interface MaterielLocationDao {

    @Insert
    fun insertMaterielLocation(materielLocation: MaterielLocation): Long

    @Update
    fun updateMaterielLocation(materielLocation: MaterielLocation)

    @Query("SELECT * FROM materiel_location")
    fun getAllFromMaterielLocation(): List<MaterielLocation>

    @Query("SELECT * FROM materiel_location")
    fun getAllFromMaterielLocationLiveData(): LiveData<List<MaterielLocation>>


    @Query("SELECT * FROM materiel_location WHERE id  = :id")
    fun getMaterielLocationById(id: Long): MaterielLocation

    @Query("SELECT * FROM materiel_location WHERE id  IN (:listIds)")
    fun getMaterielLocationByIds(listIds: List<Int>): List<MaterielLocation>
}