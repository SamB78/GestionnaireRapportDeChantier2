package com.example.gestionnairerapportdechantier.chantiers.listeChantiers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.Database.ChantierDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ListeChantiersViewModel(private val dataSource: ChantierDao) : ViewModel() {

    enum class navigationMenu {
        CREATION,
        MODIFICATION,
        EN_ATTENTE
    }

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val listeChantiers = dataSource.getAllFromChantier()

    private var _navigationPersonnel = MutableLiveData<navigationMenu>()
    val navigation: LiveData<navigationMenu>
        get() = this._navigationPersonnel

    private var _idChantier = MutableLiveData<Long>()
    val idChantier: LiveData<Long>
        get() = this._idChantier

    init {
        onBoutonClicked()
    }

    fun onClickBoutonAjoutChantier() {
        _navigationPersonnel.value =
            navigationMenu.CREATION
    }

    fun onBoutonClicked() {
        _navigationPersonnel.value =
            navigationMenu.EN_ATTENTE
    }

    fun onChantierClicked(id: Long) {
        _idChantier.value = id
        _navigationPersonnel.value = navigationMenu.MODIFICATION
    }

    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}