package com.example.gestionnairerapportdechantier.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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
    fun getAssociationPersonnelRapportChantierByPersonnelId(personnelId: Long, rapportChantierId: Long): AssociationPersonnelRapportChantier
}