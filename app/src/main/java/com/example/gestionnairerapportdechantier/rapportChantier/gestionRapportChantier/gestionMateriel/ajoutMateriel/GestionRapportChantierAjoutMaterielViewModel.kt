package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriel.ajoutMateriel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.AssociationMaterielRapportChantierDao
import com.example.gestionnairerapportdechantier.database.MaterielDao
import com.example.gestionnairerapportdechantier.entities.Materiel
import kotlinx.coroutines.*


class GestionRapportChantierAjoutMaterielViewModel(
    val rapportChantierId: Long,
    private val dataSourceMateriel: MaterielDao,
    private val dataSourceAssociationMaterielRapportChantierDao: AssociationMaterielRapportChantierDao
) : ViewModel() {

    enum class navigationMenu {
        VALIDATION,
        ANNULATION,
        EN_ATTENTE
    }

    private var _navigation = MutableLiveData<navigationMenu>()
    val navigation: LiveData<navigationMenu>
        get() = this._navigation

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    lateinit var listeMateriel: LiveData<List<Materiel>>

    private var _listeMaterielAjoutable = MutableLiveData<MutableList<Materiel>>()
    val listeMaterielAjoutable: LiveData<MutableList<Materiel>>
        get() = this._listeMaterielAjoutable


    init {
        onBoutonClicked()
    }

    private fun initializeData(rapportChantierId: Long) {
        var mutableListOfListeMateriel = mutableListOf<Materiel>()
        lateinit var listeMaterielDejaAjoute: List<Materiel>
        uiScope.launch {
            getListeMateriel()
            mutableListOfListeMateriel = listeMateriel.value as MutableList<Materiel>
            listeMaterielDejaAjoute = getListeMaterielDejaAjoute(rapportChantierId)
            mutableListOfListeMateriel.removeAll(listeMaterielDejaAjoute)
            _listeMaterielAjoutable.value = mutableListOfListeMateriel
        }

    }

    private suspend fun getListeMateriel() {
        withContext(Dispatchers.IO) {
            listeMateriel = dataSourceMateriel.getAllFromMateriel()
        }
    }

    private suspend fun getListeMaterielDejaAjoute(rapportChantierId: Long): List<Materiel> {
        return withContext(Dispatchers.IO) {
            val listeMaterielIdsDejaAjoute =
                dataSourceAssociationMaterielRapportChantierDao.getMaterielIdsByRapportChantierId(
                    rapportChantierId
                )
            val listeMaterielDejaAjoute =
                dataSourceMateriel.getMaterielByIds(listeMaterielIdsDejaAjoute)

            listeMaterielDejaAjoute
        }
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