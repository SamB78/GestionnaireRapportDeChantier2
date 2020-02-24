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
    fun insert(personnel: Personnel)

    @Update
    fun update(personnel: Personnel)

    @Query("SELECT * FROM personnel")
    fun getAllFromPersonnel(): LiveData<List<Personnel>>



}