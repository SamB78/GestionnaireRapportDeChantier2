package com.example.gestionnairerapportdechantier.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionnairerapportdechantier.entities.RapportChantier

@Dao
interface RapportChantierDao {

    @Insert
    fun insert(rapportChantier: RapportChantier)

    @Insert
    fun update(rapportChantier: RapportChantier)


    @Query("SELECT * FROM rappport_chantier")
    fun getAllFromChantier(): LiveData<List<RapportChantier>>

    @Query("SELECT * FROM rappport_chantier WHERE rapport_chantier_id = :id")
    fun getChantierById(id: Long): RapportChantier





}