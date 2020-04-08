package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.Database.RapportChantierDao

class GestionRapportChantierViewModelFactory(
    private val dataSource: RapportChantierDao,
    private val idRapportChantier: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GestionRapportChantierViewModel::class.java)) {
            return GestionRapportChantierViewModel(
                dataSource, idRapportChantier
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}