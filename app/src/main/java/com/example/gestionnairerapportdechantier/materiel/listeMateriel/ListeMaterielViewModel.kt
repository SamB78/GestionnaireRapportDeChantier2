package com.example.gestionnairerapportdechantier.materiel.listeMateriel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.MaterielDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber

class ListeMaterielViewModel(private val dataSource: MaterielDao) : ViewModel() {

    enum class navigationMenuMateriel {
        CREATION_MATERIEL,
        MODIFICATION_MATERIEL,
        CHOIX_DATE_IMMAT,
        EN_ATTENTE
    }

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val listeMateriel = dataSource.getAllFromMateriel()

    private var _navigationMateriel = MutableLiveData<navigationMenuMateriel>()
    val navigationMateriel: LiveData<navigationMenuMateriel>
        get() = this._navigationMateriel

    private var _idMateriel = MutableLiveData<Long>()
    val idMateriel: LiveData<Long>
        get() = this._idMateriel

    init {
        onBoutonClicked()
    }

    fun onClickBoutonAjoutMateriel() {
        Timber.i("Passage creation Materiel ViewModel")
        _navigationMateriel.value =
            navigationMenuMateriel.CREATION_MATERIEL

    }

    fun onBoutonClicked() {
        _navigationMateriel.value =
            navigationMenuMateriel.EN_ATTENTE

    }

    fun onMaterielClicked(id: Long) {
        _idMateriel.value = id
        _navigationMateriel.value =
            navigationMenuMateriel.MODIFICATION_MATERIEL
    }




    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}