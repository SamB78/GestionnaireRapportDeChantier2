package com.example.gestionnairerapportdechantier.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestionnairerapportdechantier.entities.Chantier

@Dao
interface ChantierDao {

    @Insert
    fun insert(chantier: Chantier)

    @Update
    fun update(chantier: Chantier)

    @Query("SELECT * FROM chantier")
    fun getAllFromChantier(): LiveData<List<Chantier>>

    @Query("SELECT * FROM chantier WHERE chantier_id = :id")
    fun getChantierById(id: Long): Chantier


}