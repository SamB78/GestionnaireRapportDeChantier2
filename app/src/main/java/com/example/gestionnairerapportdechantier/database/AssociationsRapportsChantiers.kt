package com.example.gestionnairerapportdechantier.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestionnairerapportdechantier.entities.AssociationMaterielRapportChantier
import com.example.gestionnairerapportdechantier.entities.AssociationPersonnelRapportChantier
import com.example.gestionnairerapportdechantier.entities.Chantier

@Dao
interface AssociationPersonnelRapportChantierDao {

    @Insert
    fun insertAssociationPersonnelRapportChantier(associationPersonnelRapportChantier: AssociationPersonnelRapportChantier): Long

    @Update
    fun updateAssociationPersonnelRapportChantier(associationPersonnelRapportChantier: AssociationPersonnelRapportChantier)

    @Update
    fun updateListAssociationPersonnelRapportChantier(listAssociationPersonnelRapportChantier: List<AssociationPersonnelRapportChantier>)


    @Query("SELECT * FROM assocation_personnel_rapport_chantier WHERE personnel_id = :personnelId AND rapport_chantier_id = :rapportChantierId")
    fun getAssociationPersonnelRapportChantierByPersonnelId(
        personnelId: Long,
        rapportChantierId: Long
    ): AssociationPersonnelRapportChantier
}

@Dao
interface AssociationMaterielRapportChantierDao {

    @Insert
    fun insertAssociationMaterielRapportChantier(associationMaterielRapportChantier: AssociationMaterielRapportChantier): Long

    @Update
    fun updateAssociationMaterielRapportChantier(associationMaterielRapportChantier: AssociationMaterielRapportChantier)

    @Update
    fun updateListAssociationMaterielRapportChantier(listAssociationMaterielRapportChantier: List<AssociationMaterielRapportChantier>)

    @Query("SELECT * FROM assocation_materiel_rapport_chantier WHERE materiel_id = :materielId AND rapport_chantier_id = :rapportChantierId")
    fun getAssociationMaterielRapportChantierByPersonnelId(
        materielId: Long,
        rapportChantierId: Long
    ): AssociationMaterielRapportChantier

    @Query("SELECT * FROM assocation_materiel_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getAssociationsMaterielRapportChantierByRapportChantierId(
        rapportChantierId: Long
    ): List<AssociationMaterielRapportChantier>


    @Query("SELECT associationId FROM assocation_materiel_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getAssociationsMaterielRapportChantierIdsByRapportChantierId(
        rapportChantierId: Long
    ): List<Int>

    @Query("SELECT materiel_id FROM assocation_materiel_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getMaterielIdsByRapportChantierId(
        rapportChantierId: Long
    ): List<Int>
}