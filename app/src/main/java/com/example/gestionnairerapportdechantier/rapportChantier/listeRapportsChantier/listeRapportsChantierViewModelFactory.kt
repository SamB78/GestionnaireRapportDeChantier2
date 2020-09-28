package com.example.gestionnairerapportdechantier.rapportChantier.listeRapportsChantier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.RapportChantierDao

class ListeRapportsChantierViewModelFactory(
    private val dataSource: RapportChantierDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListeRapportsChantierViewModel::class.java)) {
            return ListeRapportsChantierViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}