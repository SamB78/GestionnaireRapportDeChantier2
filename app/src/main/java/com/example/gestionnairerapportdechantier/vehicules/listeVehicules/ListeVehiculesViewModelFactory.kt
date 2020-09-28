package com.example.gestionnairerapportdechantier.vehicules.listeVehicules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.VehiculeDao

class ListeVehiculesViewModelFactory(
    private val dataSource: VehiculeDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListeVehiculesViewModel::class.java)) {
            return ListeVehiculesViewModel(
                dataSource
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
