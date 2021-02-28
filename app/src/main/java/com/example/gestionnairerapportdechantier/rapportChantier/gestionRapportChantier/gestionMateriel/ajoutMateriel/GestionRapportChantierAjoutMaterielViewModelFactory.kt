package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriel.ajoutMateriel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.AssociationMaterielRapportChantierDao
import com.example.gestionnairerapportdechantier.database.MaterielDao
import com.example.gestionnairerapportdechantier.database.RapportChantierDao

class GestionRapportChantierAjoutMaterielViewModelFactory(
    val rapportChantierId: Long,
    private val dataSourceMateriel: MaterielDao,
    private val dataSourceAssociationMaterielRapportChantierDao: AssociationMaterielRapportChantierDao,
    private val dataSourceRapportChantierDao: RapportChantierDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GestionRapportChantierAjoutMaterielViewModel::class.java)) {
            return GestionRapportChantierAjoutMaterielViewModel(
                rapportChantierId,
                dataSourceMateriel,
                dataSourceAssociationMaterielRapportChantierDao,
                dataSourceRapportChantierDao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}