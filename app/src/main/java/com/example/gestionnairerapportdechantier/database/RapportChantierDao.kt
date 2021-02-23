package com.example.gestionnairerapportdechantier.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gestionnairerapportdechantier.entities.AssociationTacheEntretienRapportChantier
import com.example.gestionnairerapportdechantier.entities.RapportChantier
import com.example.gestionnairerapportdechantier.entities.TacheEntretien
import com.example.gestionnairerapportdechantier.entities.TraitementPhytosanitaire
import java.time.LocalDate

@Dao
interface RapportChantierDao {

    @Insert
    fun insert(rapportChantier: RapportChantier): Long

    @Update
    fun update(rapportChantier: RapportChantier)


    @Query("SELECT * FROM rapport_chantier")
    fun getAllFromRapportChantierLiveData(): LiveData<List<RapportChantier>>

    @Query("SELECT * FROM rapport_chantier")
    fun getAllFromRapportChantier(): List<RapportChantier>

    @Query("SELECT * FROM rapport_chantier WHERE chantier_id = :idChantier")
    fun getAllFromRapportChantierByChantierId(idChantier: Long): List<RapportChantier>

    @Query("SELECT * FROM rapport_chantier WHERE rapport_chantier_id = :id")
    fun getRapportChantierById(id: Long): RapportChantier

    @Query("SELECT * FROM rapport_chantier WHERE date_rapport_chantier = :date")
    fun getRapportChantierByDate(date: LocalDate): RapportChantier?

    @Query("SELECT * FROM rapport_chantier WHERE rapport_chantier_id = :id")
    fun getRapportChantierLiveDataById(id: Long): LiveData<RapportChantier>

    @Query("UPDATE rapport_chantier SET chantier = :boolean WHERE rapport_chantier_id = :id")
    fun updateChantierBooleanFromId(id: Long, boolean: Boolean)

    @Query(" SELECT chantier FROM rapport_chantier WHERE rapport_chantier_id = :id ")
    fun getChantierBooleanFromId(id: Long): Boolean

    @Query("UPDATE rapport_chantier SET entretien = :boolean WHERE rapport_chantier_id = :id ")
    fun updateEntretienBooleanFromId(id: Long, boolean: Boolean)

    @Query(" SELECT entretien FROM rapport_chantier WHERE rapport_chantier_id = :id ")
    fun getEntretienBooleanFromId(id: Long): Boolean

    @Query("SELECT * FROM rapport_chantier WHERE chantier_id = :chantierId AND date_rapport_chantier IN (:list)")
    fun getRapportsChantiersFromDatesList(
        list: List<LocalDate>,
        chantierId: Long
    ): List<RapportChantier>


    //// TRAITEMENT PHYTO ////

    @Query("UPDATE rapport_chantier SET traitement_phytosanitaire_id = :idTraitement WHERE rapport_chantier_id = :id")
    fun updateRapportChantierTraitementPhytosanitaire(id: Int, idTraitement: Int?)

    @Query("SELECT * FROM traitement_phytosanitaire WHERE id = :id")
    fun getTraitementPhytosanitaireById(id: Long): TraitementPhytosanitaire

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTraitementPhytosanitaire(traitementPhytosanitaire: TraitementPhytosanitaire): Long

    @Update
    fun updateTraitementPhytosanitaire(traitementPhytosanitaire: TraitementPhytosanitaire): Int

    @Delete
    fun deleteTraitementPhytosanitaire(traitementPhytosanitaire: TraitementPhytosanitaire)


    //// TACHES ENTRETIEN ////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTachesEntretien(tachesEntretien: List<TacheEntretien>)

    @Query("SELECT * FROM tache_entretien")
    fun getAllFromTacheEntretien(): List<TacheEntretien>

    @Query("SELECT * FROM associationtacheentretienrapportchantier WHERE rapportChantierId = :id")
    fun getAllAssociationsTacheEntretienRapportChantierFromRapportChantierId(id: Int): List<AssociationTacheEntretienRapportChantier>

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    suspend fun insertAssociationsTacheEntretienRapportChantier(tachesEntretien: List<AssociationTacheEntretienRapportChantier>)

    @Delete
    suspend fun deleteAssociationsTacheEntretienRapportChantier(tachesEntretien: List<AssociationTacheEntretienRapportChantier>)

}