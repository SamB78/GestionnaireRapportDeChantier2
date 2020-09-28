package com.example.gestionnairerapportdechantier.chantiers.creationChantier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.AssociationPersonnelChantierDao
import com.example.gestionnairerapportdechantier.database.ChantierDao
import com.example.gestionnairerapportdechantier.database.PersonnelDao

class CreationChantierViewModelFactory(
    private val dataSourceChantier: ChantierDao,
    private val dataSourcePersonnel: PersonnelDao,
    private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao,
    private val idPersonnel: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreationChantierViewModel::class.java)) {
            return CreationChantierViewModel(
                dataSourceChantier, dataSourcePersonnel, dataSourceAssociationPersonnelChantier, idPersonnel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}