package com.example.gestionnairerapportdechantier.personnel.gestionPersonnel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.Database.PersonnelDao
import com.example.gestionnairerapportdechantier.entities.Personnel
import kotlinx.coroutines.*
import timber.log.Timber

class GestionPersonnelViewModel(private val dataSource: PersonnelDao, idPersonnel: Long = -1L): ViewModel() {

    enum class navigationGestionPersonnel {
        ANNULATION,
        ENREGISTREMENT_PERSONNEL,
        EN_ATTENTE
    }


    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var personnel = MutableLiveData<Personnel>()


    //navigation
    private var _navigationPersonnel =
        MutableLiveData<navigationGestionPersonnel>()
    val navigationPersonnel: LiveData<navigationGestionPersonnel>
        get() = this._navigationPersonnel


    init {

        initializePersonnel(idPersonnel)
        onBoutonClicked()
    }


    private fun initializePersonnel(id: Long){
        if(id != -1L) {
            uiScope.launch {
                personnel.value = getPersonnelValue(id)
            }
        }
        else{
            personnel.value = Personnel()
        }
    }

    private suspend fun getPersonnelValue(id: Long): Personnel? {
        return withContext(Dispatchers.IO){
            var personnel = dataSource.getPersonnelById(id)
            personnel
        }
    }


    fun onBoutonClicked() {
        _navigationPersonnel.value = navigationGestionPersonnel.EN_ATTENTE
    }


    //DELETE AND USE DATABINDING
    fun onCheckedSwitchChefEquipeChanged( check: Boolean){
        personnel.value?.fonction = check
        Timber.i("personnel = ${personnel.value?.fonction}")

    }


    fun onClickButtonCreationOrModificationEnded() {

        Timber.i("personnel ready to save in DB = ${personnel.value?.prenom}")
        if (personnel.value?.personnelId == null) sendNewPersonnelToDB()
        else updatePersonnelInDB()

        _navigationPersonnel.value = navigationGestionPersonnel.ENREGISTREMENT_PERSONNEL
    }


    private fun updatePersonnelInDB() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updatePersonnel(personnel.value!!)
            }
        }
    }


    private fun sendNewPersonnelToDB(){
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.insertPersonnel(personnel.value!!)
            }
        }
    }

    fun onClickButtonAnnuler(){
        _navigationPersonnel.value = navigationGestionPersonnel.ANNULATION
    }


    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }

}