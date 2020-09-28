package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.*

class GestionRapportChantierViewModelFactory(
    private val dataSourceRapportChantier: RapportChantierDao,
    private val dataSourceChantier: ChantierDao,
    private val dataSourcePersonnel: PersonnelDao,
    private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao,
    private val associationPersonnelRapportChantierDao: AssociationPersonnelRapportChantierDao,
    private val idRapportChantier: Long,
    private val idChantier: Int,
    private val dateRapportChantier: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GestionRapportChantierViewModel::class.java)) {
            return GestionRapportChantierViewModel(
                dataSourceRapportChantier,
                dataSourceChantier,
                dataSourcePersonnel,
                dataSourceAssociationPersonnelChantier,
                associationPersonnelRapportChantierDao,
                idRapportChantier,
                idChantier,
                dateRapportChantier
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}