package com.example.gestionnairerapportdechantier.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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



}