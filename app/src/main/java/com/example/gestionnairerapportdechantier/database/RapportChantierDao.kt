package com.example.gestionnairerapportdechantier.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestionnairerapportdechantier.entities.RapportChantier
import java.time.LocalDate

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

    @Query("SELECT * FROM rappport_chantier WHERE date_rapport_chantier = :date")
    fun getRapportChantierByDate(date: LocalDate): RapportChantier?

    @Query("SELECT * FROM rappport_chantier WHERE rapport_chantier_id = :id")
    fun getRapportChantierLiveDataById(id: Long): LiveData<RapportChantier>

    @Query("UPDATE rappport_chantier SET chantier = :boolean WHERE rapport_chantier_id = :id")
    fun updateChantierBooleanFromId(id: Long, boolean: Boolean)

    @Query(" SELECT chantier FROM rappport_chantier WHERE rapport_chantier_id = :id ")
    fun getChantierBooleanFromId(id: Long): Boolean

    @Query("UPDATE rappport_chantier SET entretien = :boolean WHERE rapport_chantier_id = :id ")
    fun updateEntretienBooleanFromId(id: Long, boolean: Boolean)

    @Query(" SELECT entretien FROM rappport_chantier WHERE rapport_chantier_id = :id ")
    fun getEntretienBooleanFromId(id: Long): Boolean

    @Query("SELECT * FROM rappport_chantier WHERE chantier_id = :chantierId AND date_rapport_chantier IN (:list)")
    fun getRapportsChantiersFromDatesList(list: List<LocalDate>, chantierId: Long):List<RapportChantier>





}