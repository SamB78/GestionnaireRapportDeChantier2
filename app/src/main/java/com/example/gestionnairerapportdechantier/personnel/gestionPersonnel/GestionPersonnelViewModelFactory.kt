package com.example.gestionnairerapportdechantier.personnel.gestionPersonnel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.PersonnelDao

class GestionPersonnelViewModelFactory(
    private val dataSource: PersonnelDao,
    private val idPersonnel: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GestionPersonnelViewModel::class.java)) {
            return GestionPersonnelViewModel(
                dataSource, idPersonnel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
