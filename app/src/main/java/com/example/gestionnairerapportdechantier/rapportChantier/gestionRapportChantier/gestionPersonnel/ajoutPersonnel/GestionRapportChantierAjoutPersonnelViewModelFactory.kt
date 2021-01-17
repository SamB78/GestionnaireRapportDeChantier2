package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionPersonnel.ajoutPersonnel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.AssociationPersonnelRapportChantierDao
import com.example.gestionnairerapportdechantier.database.PersonnelDao

class GestionRapportChantierAjoutPersonnelViewModelFactory(
    val rapportChantierId: Long,
    private val dataSourcePersonnel: PersonnelDao,
    private val dataSourceAssociationPersonnelRapportChantierDao: AssociationPersonnelRapportChantierDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GestionRapportChantierAjoutPersonnelViewModel::class.java)) {
            return GestionRapportChantierAjoutPersonnelViewModel(
                rapportChantierId,
                dataSourcePersonnel,
                dataSourceAssociationPersonnelRapportChantierDao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}