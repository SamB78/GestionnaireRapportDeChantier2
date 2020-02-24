package com.example.gestionnairerapportdechantier.gestionPersonnel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.Database.PersonnelDao

class GestionPersonnelViewModelFactory(
    private val dataSource: PersonnelDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GestionPersonnelViewModel::class.java)) {
            return GestionPersonnelViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}