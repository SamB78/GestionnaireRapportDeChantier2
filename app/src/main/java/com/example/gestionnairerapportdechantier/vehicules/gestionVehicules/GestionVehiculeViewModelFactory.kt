package com.example.gestionnairerapportdechantier.vehicules.gestionVehicules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairerapportdechantier.database.VehiculeDao

class GestionVehiculeViewModelFactory(
    private val dataSource: VehiculeDao,
    private val idVehicule: Long
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GestionVehiculeViewModel::class.java)) {
            return GestionVehiculeViewModel(
                dataSource, idVehicule
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}