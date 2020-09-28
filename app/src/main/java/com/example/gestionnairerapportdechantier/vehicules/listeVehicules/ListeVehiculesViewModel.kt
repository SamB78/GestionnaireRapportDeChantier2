package com.example.gestionnairerapportdechantier.vehicules.listeVehicules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.VehiculeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ListeVehiculesViewModel(private val dataSource: VehiculeDao) : ViewModel() {

    enum class navigationMenuVehicules {
        CREATION_VEHICULE,
        MODIFICATION_VEHICULE,
        CHOIX_DATE_IMMAT,
        EN_ATTENTE
    }

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val listeVehicules = dataSource.getAllFromVehicules()

    private var _navigationVehicules = MutableLiveData<navigationMenuVehicules>()
    val navigationVehicules: LiveData<navigationMenuVehicules>
        get() = this._navigationVehicules

    private var _idVehicule = MutableLiveData<Long>()
    val idVehicule: LiveData<Long>
        get() = this._idVehicule

    init {
        onBoutonClicked()
    }

    fun onClickBoutonAjoutVehicule() {
        _navigationVehicules.value =
            navigationMenuVehicules.CREATION_VEHICULE

    }

    fun onBoutonClicked() {
        _navigationVehicules.value =
            navigationMenuVehicules.EN_ATTENTE

    }

    fun onVehiculeClicked(id: Long) {
        _idVehicule.value = id
        _navigationVehicules.value =
            navigationMenuVehicules.MODIFICATION_VEHICULE
    }




    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}