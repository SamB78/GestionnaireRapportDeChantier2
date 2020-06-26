package com.example.gestionnairerapportdechantier.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestionnairerapportdechantier.entities.AssociationPersonnelChantier
import com.example.gestionnairerapportdechantier.entities.Chantier
import com.example.gestionnairerapportdechantier.entities.Personnel

@Dao
interface PersonnelDao {

    @Insert
    fun insertPersonnel(personnel: Personnel): Long

    @Update
    fun updatePersonnel(personnel: Personnel)

    @Query("SELECT * FROM personnel")
    fun getAllFromPersonnel(): LiveData<List<Personnel>>

    @Query("SELECT * FROM personnel")
    fun getAllFromPersonnel2(): List<Personnel>

    @Query("SELECT * FROM personnel WHERE personnel_id  = :id")
    fun getPersonnelById(id: Long): Personnel


    @Query("SELECT * FROM personnel WHERE chefEquipe = 1 ")
    fun getChefsdeChantier(): LiveData<List<Personnel>>

    @Query("SELECT * FROM personnel WHERE personnel_id IN (:listIds)")
    fun getPersonnelsByIds(listIds: List<Long>): List<Personnel>

}