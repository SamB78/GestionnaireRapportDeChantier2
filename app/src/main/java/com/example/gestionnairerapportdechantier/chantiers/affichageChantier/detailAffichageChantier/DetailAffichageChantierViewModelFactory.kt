package com.example.gestionnairerapportdechantier.chantiers.affichageChantier.detailAffichageChantier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.AssociationPersonnelChantierDao
import com.example.gestionnairerapportdechantier.database.ChantierDao
import com.example.gestionnairerapportdechantier.database.PersonnelDao
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.AffichageChantierViewModel
import com.example.gestionnairerapportdechantier.database.RapportChantierDao

class DetailAffichageChantierViewModelFactory(
    private val dataSourceChantier: ChantierDao,
    private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao,
    private val dataSourcePersonnel: PersonnelDao,
    private val dataSourceRapporChantier: RapportChantierDao,
    private val chantierId: Long = -1

) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AffichageChantierViewModel::class.java)) {
            return AffichageChantierViewModel(
                dataSourceChantier,
                dataSourceAssociationPersonnelChantier,
                dataSourcePersonnel,
                dataSourceRapporChantier,
                chantierId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}