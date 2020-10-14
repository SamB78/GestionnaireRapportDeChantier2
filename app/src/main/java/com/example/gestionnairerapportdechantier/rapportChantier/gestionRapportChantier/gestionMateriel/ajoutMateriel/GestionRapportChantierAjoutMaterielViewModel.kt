package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriel.ajoutMateriel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.AssociationMaterielRapportChantierDao
import com.example.gestionnairerapportdechantier.database.MaterielDao
import com.example.gestionnairerapportdechantier.entities.AssociationMaterielRapportChantier
import com.example.gestionnairerapportdechantier.entities.Materiel
import kotlinx.coroutines.*
import timber.log.Timber


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


    var listeMateriel = MutableLiveData<List<Materiel>>(emptyList())
    var listeMaterielAjoutable = MutableLiveData<MutableList<Materiel>>()

    //Equivalent pour _listeMaterielAjoutable
    var listeMaterielAAfficher = MutableLiveData<List<Materiel>>(emptyList())


    init {
        Timber.i("Passage Init")
        listeMaterielAjoutable.value = mutableListOf<Materiel>()
        onBoutonClicked()
//        initializeData(rapportChantierId)
    }

    fun initializeData(rapportChantierId: Long) {
        listeMaterielAjoutable.value = mutableListOf<Materiel>()
        var mutableListOfListeMateriel: MutableList<Materiel> = mutableListOf<Materiel>()
        lateinit var listeMaterielDejaAjoute: List<Materiel>
        uiScope.launch {
            listeMateriel.value = getListeMaterielWithReturn()
            mutableListOfListeMateriel.addAll(listeMateriel.value as MutableList<Materiel>)

            listeMaterielDejaAjoute = getListeMaterielDejaAjoute(rapportChantierId)
            mutableListOfListeMateriel.removeAll(listeMaterielDejaAjoute)
            listeMaterielAjoutable.value = mutableListOfListeMateriel

            Timber.i("RapportChantierId: $rapportChantierId")


            listeMaterielAjoutable.value!!.forEach {
                Timber.i("listeMaterielAjoutable = $it")
            }

        }

    }

    private suspend fun getListeMateriel() {
        withContext(Dispatchers.IO) {
            listeMateriel.postValue(dataSourceMateriel.getAllFromMateriel())
        }
    }

    private suspend fun getListeMaterielWithReturn(): List<Materiel> {
        return withContext(Dispatchers.IO) {
            var listeMateriel = dataSourceMateriel.getAllFromMateriel()

            listeMateriel
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
            var associationMaterielRapportChantier = AssociationMaterielRapportChantier(null, 0, 0)

            listeMaterielAjoutable.value!!.filter { it.isChecked }.forEach {
                associationMaterielRapportChantier.materielId = it.id!!
                associationMaterielRapportChantier.rapportChantierID = rapportChantierId.toInt()
                Timber.i("associationMaterielRapportChantier = $associationMaterielRapportChantier")
                saveAssociation(associationMaterielRapportChantier)
            }
        }
        _navigation.value = navigationMenu.VALIDATION

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

    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }


}