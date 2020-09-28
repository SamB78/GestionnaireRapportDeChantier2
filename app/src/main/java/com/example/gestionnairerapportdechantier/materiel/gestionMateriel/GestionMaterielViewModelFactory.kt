package com.example.gestionnairerapportdechantier.materiel.gestionMateriel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.MaterielDao

class GestionMaterielViewModelFactory(
    private val dataSource: MaterielDao,
    private val idMateriel: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GestionMaterielViewModel::class.java)) {
            return GestionMaterielViewModel(
                dataSource, idMateriel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}