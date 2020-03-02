package com.example.gestionnairerapportdechantier.chantiers.gestionChantiers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.Database.ChantierDao

class GestionChantierViewModelFactory(
    private val dataSource: ChantierDao,
    private val idPersonnel: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GestionChantierViewModel::class.java)) {
            return GestionChantierViewModel(
                dataSource, idPersonnel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}