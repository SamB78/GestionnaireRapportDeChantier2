package com.example.gestionnairerapportdechantier.materiel.listeMateriel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.MaterielDao

class ListeMaterielViewModelFactory(
    private val dataSource: MaterielDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListeMaterielViewModel::class.java)) {
            return ListeMaterielViewModel(
                dataSource
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}