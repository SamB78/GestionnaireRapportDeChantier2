package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionPersonnel.ajoutPersonnel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.AssociationPersonnelRapportChantierDao
import com.example.gestionnairerapportdechantier.database.PersonnelDao
import com.example.gestionnairerapportdechantier.entities.AssociationPersonnelRapportChantier
import com.example.gestionnairerapportdechantier.entities.Personnel
import kotlinx.coroutines.*
import timber.log.Timber

class GestionRapportChantierAjoutPersonnelViewModel(
    val rapportChantierId: Long,
    private val dataSourcePersonnel: PersonnelDao,
    private val dataSourceAssociationPersonnelRapportChantierDao: AssociationPersonnelRapportChantierDao
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

    var listePersonnel = MutableLiveData<MutableList<Personnel>>(mutableListOf())
    var listePersonnelAjoutable = MutableLiveData<MutableList<Personnel>>(mutableListOf())

    init {
        Timber.i("Passage Init")
        onBoutonClicked()
        initializeData(rapportChantierId)
    }


    private fun initializeData(rapportChantierId: Long) {
        uiScope.launch {
            listePersonnel.value = getListePersonnel() as MutableList<Personnel>
            listePersonnel.value?.removeAll(getListePersonnelDejaAjoute(rapportChantierId))
            listePersonnelAjoutable.value = listePersonnel.value

        }
    }

    private suspend fun getListePersonnelDejaAjoute(rapportChantierId: Long): List<Personnel> {
        return withContext(Dispatchers.IO) {
            val listePersonnelIdsDejaAjoute =
                dataSourceAssociationPersonnelRapportChantierDao.getPersonnelIdsByRapportChantierId(
                    rapportChantierId
                )
            val listeMaterielDejaAjoute =
                dataSourcePersonnel.getPersonnelsByIds(listePersonnelIdsDejaAjoute)

            listeMaterielDejaAjoute
        }
    }

    private suspend fun getListePersonnel(): List<Personnel>? {
        return withContext(Dispatchers.IO) {
            val listePersonnel = dataSourcePersonnel.getAllFromPersonnel2()
            listePersonnel
        }

    }

    fun onClickPersonnel(personnel: Personnel) {
        listePersonnelAjoutable.value!!.find { it.personnelId == personnel.personnelId }?.isChecked =
            !personnel.isChecked
        listePersonnelAjoutable.value = listePersonnelAjoutable.value
    }

    fun onClickEnregistrer() {
        uiScope.launch {
            val associationPersonnelRapportChantier = AssociationPersonnelRapportChantier(null, 0,0)
            listePersonnelAjoutable.value!!.filter { it.isChecked }.forEach {
                associationPersonnelRapportChantier.personnelId = it.personnelId!!
                associationPersonnelRapportChantier.rapportChantierID = rapportChantierId.toInt()
                saveAssociation(associationPersonnelRapportChantier)
            }

            _navigation.value = navigationMenu.VALIDATION
        }
    }

    private suspend fun saveAssociation(associationPersonnelRapportChantier: AssociationPersonnelRapportChantier) {
        withContext(Dispatchers.IO){
            dataSourceAssociationPersonnelRapportChantierDao.insertAssociationPersonnelRapportChantier(
                associationPersonnelRapportChantier
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