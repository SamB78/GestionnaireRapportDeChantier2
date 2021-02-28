package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriel.ajoutMateriel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.AssociationMaterielRapportChantierDao
import com.example.gestionnairerapportdechantier.database.MaterielDao
import com.example.gestionnairerapportdechantier.database.RapportChantierDao
import com.example.gestionnairerapportdechantier.entities.AssociationMaterielRapportChantier
import com.example.gestionnairerapportdechantier.entities.Materiel
import com.example.gestionnairerapportdechantier.entities.RapportChantier
import kotlinx.coroutines.*
import timber.log.Timber


class GestionRapportChantierAjoutMaterielViewModel(
    val rapportChantierId: Long,
    private val dataSourceMateriel: MaterielDao,
    private val dataSourceAssociationMaterielRapportChantierDao: AssociationMaterielRapportChantierDao,
    private val dataSourceRapportChantier: RapportChantierDao
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

    private val rapportChantier = MutableLiveData<RapportChantier>()


    var listeMateriel = MutableLiveData<List<Materiel>>(emptyList())
    var listeMaterielAjoutable = MutableLiveData<MutableList<Materiel>>(mutableListOf<Materiel>())
    var listeMaterielToShow = MutableLiveData<MutableList<Materiel>>(mutableListOf<Materiel>())

    var searchMaterielEntretien = MutableLiveData<Boolean>(false)
    var searchMaterielChantier = MutableLiveData<Boolean>(false)

    init {
        Timber.i("Passage Init")
        onBoutonClicked()
        initializeData(rapportChantierId)

        Transformations.map(searchMaterielChantier){
            Timber.i("search Materiel Chantier")
            generateListeMaterielToShow()
        }

        Transformations.map(searchMaterielEntretien){
            Timber.i("search Materiel Entretien")
            generateListeMaterielToShow()
        }
    }

    private fun initializeData(rapportChantierId: Long) {
        val mutableListOfListeMateriel: MutableList<Materiel> = mutableListOf<Materiel>()
        uiScope.launch {
            rapportChantier.value = getRapportChantier(rapportChantierId)

            rapportChantier.value?.let {
                if (it.typeChantier == 1) searchMaterielEntretien.value = true
                else if (it.typeChantier == 2) searchMaterielChantier.value = true
            }

            listeMateriel.value = getListeMaterielWithReturn()
            mutableListOfListeMateriel.addAll(listeMateriel.value as MutableList<Materiel>)

            mutableListOfListeMateriel.removeAll(getListeMaterielDejaAjoute(rapportChantierId))
            listeMaterielAjoutable.value = mutableListOfListeMateriel
            generateListeMaterielToShow()

            listeMaterielAjoutable.value!!.forEach {
                Timber.i("listeMaterielAjoutable = $it")
            }

        }

    }

    private suspend fun getRapportChantier(id: Long) =
        withContext(Dispatchers.IO) {
            dataSourceRapportChantier.getRapportChantierById(id)
        }

    private suspend fun getListeMaterielWithReturn() =
        withContext(Dispatchers.IO) {
            dataSourceMateriel.getAllFromMateriel()

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

    fun onClickMateriel(materiel: Materiel) {
        listeMaterielAjoutable.value!!.find { it.id == materiel.id }?.isChecked =
            !materiel.isChecked

        listeMaterielAjoutable.value!!.filter { it.id == materiel.id }.forEach {
            Timber.i("Value isChecked = ${it.isChecked}")
        }

        listeMaterielAjoutable.value = listeMaterielAjoutable.value
    }

    fun onClickEnregistrer() {
        uiScope.launch {
            val associationMaterielRapportChantier = AssociationMaterielRapportChantier(null, 0, 0)

            listeMaterielAjoutable.value!!.filter { it.isChecked }.forEach {
                associationMaterielRapportChantier.materielId = it.id!!
                associationMaterielRapportChantier.rapportChantierID = rapportChantierId.toInt()
                Timber.i("associationMaterielRapportChantier = $associationMaterielRapportChantier")
                saveAssociation(associationMaterielRapportChantier)
            }
            _navigation.value = navigationMenu.VALIDATION
        }


    }

    private suspend fun saveAssociation(associationMaterielRapportChantier: AssociationMaterielRapportChantier) {
        withContext(Dispatchers.IO) {
            dataSourceAssociationMaterielRapportChantierDao.insertAssociationMaterielRapportChantier(
                associationMaterielRapportChantier
            )
        }
    }


    fun onBoutonClicked() {
        _navigation.value = navigationMenu.EN_ATTENTE
    }


    fun generateListeMaterielToShow() {
        listeMaterielToShow.value!!.clear()


        listeMaterielAjoutable.value!!.forEach {
            if (it.materielChantier && searchMaterielChantier.value!!) listeMaterielToShow.value!!.add(
                it
            )
            if (it.materielEntretien && searchMaterielEntretien.value!!) listeMaterielToShow.value!!.add(
                it
            )
        }
    }


    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }


}