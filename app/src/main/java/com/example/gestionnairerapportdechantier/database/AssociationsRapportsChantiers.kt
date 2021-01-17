package com.example.gestionnairerapportdechantier.database

import androidx.room.*
import com.example.gestionnairerapportdechantier.entities.*

@Dao
interface AssociationPersonnelRapportChantierDao {

    @Insert
    fun insertAssociationPersonnelRapportChantier(associationPersonnelRapportChantier: AssociationPersonnelRapportChantier): Long

    @Update
    fun updateAssociationPersonnelRapportChantier(associationPersonnelRapportChantier: AssociationPersonnelRapportChantier)

    @Update
    fun updateListAssociationPersonnelRapportChantier(listAssociationPersonnelRapportChantier: List<AssociationPersonnelRapportChantier>)

    @Query("DELETE FROM assocation_personnel_rapport_chantier WHERE personnel_id = :personnelId AND rapport_chantier_id = :rapportChantierId")
    fun deleteAssociationPersonnelRapportChantierByPersonnelId(
        personnelId: Long,
        rapportChantierId: Long
    )

    @Query("SELECT * FROM assocation_personnel_rapport_chantier WHERE personnel_id = :personnelId AND rapport_chantier_id = :rapportChantierId")
    fun getAssociationPersonnelRapportChantierByPersonnelId(
        personnelId: Long,
        rapportChantierId: Long
    ): AssociationPersonnelRapportChantier


    @Query("SELECT * FROM assocation_personnel_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getAssociationsPersonnelRapportChantierByRapportChantierId(
        rapportChantierId: Long
    ): List<AssociationPersonnelRapportChantier>

    @Query("SELECT * FROM assocation_personnel_rapport_chantier WHERE  rapport_chantier_id IN (:list)")
    fun getAssociationsPersonnelRapportChantierByRapportsChantierIdsList(
        list: List<Long>
    ): List<AssociationPersonnelRapportChantier>

    @Query("SELECT personnel_id FROM assocation_personnel_rapport_chantier WHERE rapport_chantier_id = :rapportChantierId")
    fun getPersonnelIdsByRapportChantierId(rapportChantierId: Long): List<Int>

}

@Dao
interface AssociationMaterielRapportChantierDao {

    @Insert
    fun insertAssociationMaterielRapportChantier(associationMaterielRapportChantier: AssociationMaterielRapportChantier): Long

    @Update
    fun updateAssociationMaterielRapportChantier(associationMaterielRapportChantier: AssociationMaterielRapportChantier)

    @Update
    fun updateListAssociationMaterielRapportChantier(listAssociationMaterielRapportChantier: List<AssociationMaterielRapportChantier>)

    @Query("DELETE FROM assocation_materiel_rapport_chantier WHERE materiel_id = :materielId AND rapport_chantier_id = :rapportChantierId ")
    fun deleteAssociationRapportChantierByMaterielId(
        materielId: Long,
        rapportChantierId: Long
    )

    @Query("SELECT * FROM assocation_materiel_rapport_chantier WHERE materiel_id = :materielId AND rapport_chantier_id = :rapportChantierId")
    fun getAssociationMaterielRapportChantierByMaterielId(
        materielId: Long,
        rapportChantierId: Long
    ): AssociationMaterielRapportChantier

    @Query("SELECT * FROM assocation_materiel_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getAssociationsMaterielRapportChantierByRapportChantierId(
        rapportChantierId: Long
    ): List<AssociationMaterielRapportChantier>

    @Query("SELECT * FROM assocation_materiel_rapport_chantier WHERE  rapport_chantier_id IN (:list)")
    fun getAssociationsMaterielRapportChantierByRapportChantierIdsLists(
        list: List<Long>
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

@Dao
interface AssociationMaterielLocationRapportChantierDao {

    @Insert
    fun insertAssociationMaterielLocationRapportChantier(associationMaterielLocationRapportChantier: AssociationMaterielLocationRapportChantier): Long

    @Update
    fun updateAssociationMaterielLocationRapportChantier(associationMaterielLocationRapportChantier: AssociationMaterielLocationRapportChantier)

    @Update
    fun updateListAssociationMaterielLocationRapportChantier(
        listAssociationMaterielLocationRapportChantier: List<AssociationMaterielLocationRapportChantier>
    )

    @Query("DELETE FROM assocation_materiel_location_rapport_chantier WHERE materiel_location_id = :materielLocationId AND rapport_chantier_id = :rapportChantierId ")
    fun deleteAssociationRapportChantierByMaterielLocationId(
        materielLocationId: Long,
        rapportChantierId: Long
    )

    @Query("SELECT * FROM assocation_materiel_location_rapport_chantier WHERE materiel_location_id = :materielLocationId AND rapport_chantier_id = :rapportChantierId")
    fun getAssociationMaterielLocationRapportChantierByMaterielLocationId(
        materielLocationId: Long,
        rapportChantierId: Long
    ): AssociationMaterielLocationRapportChantier

    @Query("SELECT * FROM assocation_materiel_location_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getAssociationsMaterielLocationRapportChantierByRapportChantierId(
        rapportChantierId: Long
    ): List<AssociationMaterielLocationRapportChantier>

    @Query("SELECT * FROM assocation_materiel_location_rapport_chantier WHERE  rapport_chantier_id IN (:list)")
    fun getAssociationsMaterielLocationRapportChantierByRapportChantierIdsLists(
        list: List<Long>
    ): List<AssociationMaterielLocationRapportChantier>


    @Query("SELECT associationId FROM assocation_materiel_location_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getAssociationsMaterielLocationRapportChantierIdsByRapportChantierId(
        rapportChantierId: Long
    ): List<Int>


    @Query("SELECT materiel_location_id FROM assocation_materiel_location_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getMaterielLocationIdsByRapportChantierId(
        rapportChantierId: Long
    ): List<Int>
}

@Dao
interface AssociationMateriauxRapportChantierDao {

    @Insert
    fun insertAssociationMateriauxRapportChantier(associationMateriauxRapportChantier: AssociationMateriauxRapportChantier): Long

    @Update
    fun updateAssociationMateriauxRapportChantier(associationMateriauxRapportChantier: AssociationMateriauxRapportChantier)

    @Update
    fun updateListAssociationMateriauxRapportChantier(listAssociationMateriauxRapportChantier: List<AssociationMateriauxRapportChantier>)

    @Query("DELETE FROM assocation_materiaux_rapport_chantier WHERE materiaux_id = :materiauxId AND rapport_chantier_id = :rapportChantierId")
    fun deleteAssociationMateriauxRapportChantierByMateriauxId(
        materiauxId: Long,
        rapportChantierId: Long
    )

    @Query("SELECT * FROM assocation_materiaux_rapport_chantier WHERE materiaux_id = :materiauxId AND rapport_chantier_id = :rapportChantierId")
    fun getAssociationMateriauxRapportChantierByMateriauxId(
        materiauxId: Long,
        rapportChantierId: Long
    ): AssociationMateriauxRapportChantier

    @Query("SELECT * FROM assocation_materiaux_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getAssociationsMateriauxRapportChantierByRapportChantierId(
        rapportChantierId: Long
    ): List<AssociationMateriauxRapportChantier>

    @Query("SELECT * FROM assocation_materiaux_rapport_chantier WHERE  rapport_chantier_id IN (:list)")
    fun getAssociationsMateriauxRapportChantierByRapportChantierIdsList(
        list: List<Long>
    ): List<AssociationMateriauxRapportChantier>


    @Query("SELECT associationId FROM assocation_materiaux_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getAssociationsMateriauxRapportChantierIdsByRapportChantierId(
        rapportChantierId: Long
    ): List<Int>


    @Query("SELECT materiaux_id FROM assocation_materiaux_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getMateriauxIdsByRapportChantierId(
        rapportChantierId: Long
    ): List<Int>


}

@Dao
interface AssociationSousTraitanceRapportChantierDao {

    @Insert
    fun insertAssociationSousTraitanceRapportChantier(associationSousTraitanceRapportChantier: AssociationSousTraitanceRapportChantier): Long

    @Update
    fun updateAssociationSousTraitanceRapportChantier(associationSousTraitanceRapportChantier: AssociationSousTraitanceRapportChantier)

    @Update
    fun updateListAssociationSousTraitanceRapportChantier(listAssociationSousTraitanceRapportChantier: List<AssociationSousTraitanceRapportChantier>)

    @Query("DELETE FROM assocation_sous_traitance_rapport_chantier WHERE sous_traitance_id = :sousTraitanceId AND rapport_chantier_id = :rapportChantierId")
    fun deleteAssociationSousTraitanceRapportChantierBySousTraitanceId(
        sousTraitanceId: Long,
        rapportChantierId: Long
    )

    @Query("SELECT * FROM assocation_sous_traitance_rapport_chantier WHERE sous_traitance_id = :sousTraitanceId AND rapport_chantier_id = :rapportChantierId")
    fun getAssociationSousTraitanceRapportChantierBySousTraitanceId(
        sousTraitanceId: Long,
        rapportChantierId: Long
    ): AssociationSousTraitanceRapportChantier

    @Query("SELECT * FROM assocation_sous_traitance_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getAssociationsSousTraitanceRapportChantierByRapportChantierId(
        rapportChantierId: Long
    ): List<AssociationSousTraitanceRapportChantier>

    @Query("SELECT * FROM assocation_sous_traitance_rapport_chantier WHERE  rapport_chantier_id IN (:list)")
    fun getAssociationsSousTraitanceRapportChantierByRapportChantierIdsList(
        list: List<Long>
    ): List<AssociationSousTraitanceRapportChantier>


    @Query("SELECT associationId FROM assocation_sous_traitance_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getAssociationsSousTraitanceRapportChantierIdsByRapportChantierId(
        rapportChantierId: Long
    ): List<Int>


    @Query("SELECT sous_traitance_id FROM assocation_sous_traitance_rapport_chantier WHERE  rapport_chantier_id = :rapportChantierId")
    fun getSousTraitanceIdsByRapportChantierId(
        rapportChantierId: Long
    ): List<Int>


}


