package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriel.ajoutMateriel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.AssociationMaterielRapportChantierDao
import com.example.gestionnairerapportdechantier.database.MaterielDao

class GestionRapportChantierAjoutMaterielViewModelFactory(
    val rapportChantierId: Long,
    private val dataSourceMateriel: MaterielDao,
    private val dataSourceAssociationMaterielRapportChantierDao: AssociationMaterielRapportChantierDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GestionRapportChantierAjoutMaterielViewModel::class.java)) {
            return GestionRapportChantierAjoutMaterielViewModel(
                rapportChantierId,
                dataSourceMateriel,
                dataSourceAssociationMaterielRapportChantierDao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}