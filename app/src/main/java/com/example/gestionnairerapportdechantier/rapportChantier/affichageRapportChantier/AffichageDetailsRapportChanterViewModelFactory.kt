package com.example.gestionnairerapportdechantier.rapportChantier.affichageRapportChantier

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.*

class AffichageDetailsRapportChanterViewModelFactory(
    private val application: Application,
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
    private val dateBegginingRapportChantier: Long,
    private val dateEnd: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AffichageDetailsRapportChantierViewModel::class.java)) {
            return AffichageDetailsRapportChantierViewModel(
                application,
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
                dateBegginingRapportChantier,
                dateEnd
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}