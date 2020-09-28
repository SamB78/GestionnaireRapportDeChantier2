package com.example.gestionnairerapportdechantier.chantiers.listeChantiers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.AssociationPersonnelChantierDao
import com.example.gestionnairerapportdechantier.database.ChantierDao

class ListeChantiersViewModelFactory (private val dataSource: ChantierDao, private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListeChantiersViewModel::class.java)) {
            return ListeChantiersViewModel(
                dataSource,
                dataSourceAssociationPersonnelChantier
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}