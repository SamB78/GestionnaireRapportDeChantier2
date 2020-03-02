package com.example.gestionnairerapportdechantier.chantiers.gestionChantiers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.Database.ChantierDao
import com.example.gestionnairerapportdechantier.entities.Chantier
import kotlinx.coroutines.*
import timber.log.Timber

class GestionChantierViewModel(private val dataSource: ChantierDao, id: Long = -1L) :
    ViewModel() {

    enum class gestionNavigation {
        ANNULATION,
        ENREGISTREMENT_CHANTIER,
        EN_ATTENTE
    }

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var chantier = MutableLiveData<Chantier>()

    //Navigation

    private var _navigation = MutableLiveData<gestionNavigation>()
    val navigation: LiveData<gestionNavigation>
        get() = _navigation

    init {
        initializeData(id)
        onBoutonClicked()
    }

    private fun initializeData(id: Long) {
        if (id != 1L) {
            uiScope.launch {
                chantier.value = getChantierValue(id)
            }
        } else {
            chantier.value = Chantier()
        }
    }

    private suspend fun getChantierValue(id: Long): Chantier? {
        return withContext(Dispatchers.IO) {
            var chantier = dataSource.getChantierById(id)
            chantier
        }
    }

    fun onBoutonClicked() {
        _navigation.value = gestionNavigation.EN_ATTENTE
    }

    fun onClickButtonCreationOrModificationEnded() {

        Timber.i("Chantier ready to save in DB = ${chantier.value}")
        if (chantier.value?.chantierId == null) sendNewDataToDB()
        else updateDataInDB()

        _navigation.value = gestionNavigation.ENREGISTREMENT_CHANTIER
    }

    private fun updateDataInDB() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.update(chantier.value!!)
            }
        }
    }

    private fun sendNewDataToDB() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.insert(chantier.value!!)
            }
        }
    }

    fun onClickButtonAnnuler() {
        _navigation.value = gestionNavigation.ANNULATION
    }


    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }


}