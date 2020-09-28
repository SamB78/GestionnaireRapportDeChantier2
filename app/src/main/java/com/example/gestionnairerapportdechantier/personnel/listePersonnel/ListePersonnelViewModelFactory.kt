package com.example.gestionnairerapportdechantier.personnel.listePersonnel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.PersonnelDao

class ListePersonnelViewModelFactory(
    private val dataSource: PersonnelDao): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListePersonnelViewModel::class.java)) {
            return ListePersonnelViewModel(
                dataSource
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}