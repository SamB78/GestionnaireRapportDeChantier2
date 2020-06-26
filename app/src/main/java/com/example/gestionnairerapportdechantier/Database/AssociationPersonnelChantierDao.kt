package com.example.gestionnairerapportdechantier.Database

import androidx.room.Dao
import androidx.room.Insert
import com.example.gestionnairerapportdechantier.entities.AssociationPersonnelChantier


@Dao
interface AssociationPersonnelChantierDao {

    @Insert
    fun insertAssociationPersonnelChantier(associationPersonnelChantier: AssociationPersonnelChantier)

    @Insert
    fun insertListAssociationPersonnelChantier(listeAssociationPersonnelChantier: List<AssociationPersonnelChantier>)


}