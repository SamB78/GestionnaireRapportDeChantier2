package com.example.gestionnairerapportdechantier.chantiers.affichageChantier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.Database.AssociationPersonnelChantierDao
import com.example.gestionnairerapportdechantier.Database.ChantierDao
import com.example.gestionnairerapportdechantier.Database.PersonnelDao

class AffichageChantierViewModelFactory(
    private val dataSourceChantier: ChantierDao,
    private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao,
    private val dataSourcePersonnel: PersonnelDao,
    private val chantierId: Long = -1

) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AffichageChantierViewModel::class.java)) {
            return AffichageChantierViewModel(
                dataSourceChantier,
                dataSourceAssociationPersonnelChantier,
                dataSourcePersonnel,
                chantierId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}