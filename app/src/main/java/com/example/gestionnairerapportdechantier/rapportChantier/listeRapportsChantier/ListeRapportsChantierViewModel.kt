package com.example.gestionnairerapportdechantier.rapportChantier.listeRapportsChantier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.RapportChantierDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ListeRapportsChantierViewModel(private val dataSourceRapporChantier: RapportChantierDao) : ViewModel() {

    enum class navigationMenu {
        CREATION,
        MODIFICATION,
        CONSULTATION,
        AJOUT,
        SELECTION_DATE,
        EN_ATTENTE
    }

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    var listeRapportChantier = dataSourceRapporChantier.getAllFromRapportChantierLiveData()

    private var _navigation = MutableLiveData<navigationMenu>()
    val navigation: LiveData<navigationMenu>
        get() = this._navigation

    private var _idRapportChantier = MutableLiveData<Long>()
    val idRapportChantier: LiveData<Long>
        get() = this._idRapportChantier

    init {
        onBoutonClicked()
    }

    fun onClickBoutonAjoutRapportChantier(){
        _navigation.value = navigationMenu.SELECTION_DATE
    }

    fun onDateSelected(){
        _navigation.value = navigationMenu.CREATION
    }

    fun onRapportChantierClicked(id: Long){
        _idRapportChantier.value = id
        _navigation.value = navigationMenu.MODIFICATION
    }

    fun onBoutonClicked() {
        _navigation.value = navigationMenu.EN_ATTENTE
    }


    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}