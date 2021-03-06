package com.example.gestionnairerapportdechantier.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestionnairerapportdechantier.entities.AssociationMaterielRapportChantier
import com.example.gestionnairerapportdechantier.entities.Materiel

@Dao
interface MaterielDao {

    @Insert
    fun insertMateriel(materiel: Materiel): Long

    @Update
    fun updateMateriel(materiel: Materiel)

    @Query("SELECT * FROM materiel")
    fun getAllFromMateriel(): List<Materiel>

    @Query("SELECT * FROM materiel")
    fun getAllFromMaterielLiveData(): LiveData<List<Materiel>>


    @Query("SELECT * FROM materiel WHERE id  = :id")
    fun getMaterielById(id: Long): Materiel

    @Query("SELECT * FROM materiel WHERE id  IN (:listIds)")
    fun getMaterielByIds(listIds: List<Int>): List<Materiel>

}