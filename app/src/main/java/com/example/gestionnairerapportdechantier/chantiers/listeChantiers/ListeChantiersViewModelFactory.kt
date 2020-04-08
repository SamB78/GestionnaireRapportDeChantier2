package com.example.gestionnairerapportdechantier.chantiers.listeChantiers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.Database.ChantierDao
import com.example.gestionnairerapportdechantier.personnel.listePersonnel.ListePersonnelViewModel

class ListeChantiersViewModelFactory (private val dataSource: ChantierDao): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListeChantiersViewModel::class.java)) {
            return ListeChantiersViewModel(
                dataSource
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}