package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.*

class GestionRapportChantierViewModelFactory(
    private val dataSourceRapportChantier: RapportChantierDao,
    private val dataSourceChantier: ChantierDao,
    private val dataSourcePersonnel: PersonnelDao,
    private val dataSourceMateriel: MaterielDao,
    private val dataSourceMaterielLocation: MaterielLocationDao,
    private val dataSourceMateriaux: MateriauxDao,
    private val dataSourceSousTraitance: SousTraitanceDao,
    private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao,
    private val associationPersonnelRapportChantierDao: AssociationPersonnelRapportChantierDao,
    private val associationMaterielRapportChantierDao: AssociationMaterielRapportChantierDao,
    private val associationMaterielLocationRapportChantierDao: AssociationMaterielLocationRapportChantierDao,
    private val dataSourceAssociationMateriauxRapportChantierDao: AssociationMateriauxRapportChantierDao,
    private val dataSourceAssociationSousTraitanceRapportChantierDao: AssociationSousTraitanceRapportChantierDao,
    private val idRapportChantier: Long,
    private val idChantier: Int,
    private val dateRapportChantier: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GestionRapportChantierViewModel::class.java)) {
            return GestionRapportChantierViewModel(
                dataSourceRapportChantier,
                dataSourceChantier,
                dataSourcePersonnel,
                dataSourceMateriel,
                dataSourceMaterielLocation,
                dataSourceMateriaux,
                dataSourceSousTraitance,
                dataSourceAssociationPersonnelChantier,
                associationPersonnelRapportChantierDao,
                associationMaterielRapportChantierDao,
                associationMaterielLocationRapportChantierDao,
                dataSourceAssociationMateriauxRapportChantierDao,
                dataSourceAssociationSousTraitanceRapportChantierDao,
                idRapportChantier,
                idChantier,
                dateRapportChantier
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}