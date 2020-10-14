package com.example.gestionnairerapportdechantier.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestionnairerapportdechantier.entities.RapportChantier

@Dao
interface RapportChantierDao {

    @Insert
    fun insert(rapportChantier: RapportChantier): Long

    @Update
    fun update(rapportChantier: RapportChantier)


    @Query("SELECT * FROM rappport_chantier")
    fun getAllFromRapportChantierLiveData(): LiveData<List<RapportChantier>>

    @Query("SELECT * FROM rappport_chantier")
    fun getAllFromRapportChantier(): List<RapportChantier>

    @Query("SELECT * FROM rappport_chantier WHERE chantier_id = :idChantier")
    fun getAllFromRapportChantierByChantierId(idChantier: Long): List<RapportChantier>

    @Query("SELECT * FROM rappport_chantier WHERE rapport_chantier_id = :id")
    fun getRapportChantierById(id: Long): RapportChantier





}