package com.example.gestionnairerapportdechantier.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionnairerapportdechantier.entities.AssociationPersonnelChantier


@Dao
interface AssociationPersonnelChantierDao {

    @Insert
    fun insertAssociationPersonnelChantier(associationPersonnelChantier: AssociationPersonnelChantier)

    @Insert
    fun insertListAssociationPersonnelChantier(listeAssociationPersonnelChantier: List<AssociationPersonnelChantier>)

    @Query("SELECT * FROM AssociationPersonnelChantier WHERE chantierID  = :id")
    fun getAssociationPersonnelChantierByChantierId(id: Int): List<AssociationPersonnelChantier>

    @Query("SELECT personnelID FROM AssociationPersonnelChantier WHERE chantierID  = :id")
    fun getAssociationPersonnelChantierIdByChantierId(id: Int): List<Int>


}