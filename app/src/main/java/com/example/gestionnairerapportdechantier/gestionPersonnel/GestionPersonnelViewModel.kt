package com.example.gestionnairerapportdechantier.gestionPersonnel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.Database.PersonnelDao
import com.example.gestionnairerapportdechantier.entities.Personnel
import kotlinx.coroutines.*
import timber.log.Timber


class GestionPersonnelViewModel(private val dataSource: PersonnelDao): ViewModel(){


    enum class navigationMenuPersonnel {
        CREATION_PERSONNEL,
        DETAIL_PERSONNEL,
        MODIFICATION_PERSONNEL,
        EN_ATTENTE,
        LISTE_PERSONNEL,
        ENREGISTREMENT_PERSONNEL
    }


    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val listePersonnel = dataSource.getAllFromPersonnel()

    var newPersonnel = MutableLiveData<Personnel>()

    private var _navigationPersonnel = MutableLiveData<navigationMenuPersonnel>()
    val navigationPersonnel: LiveData<navigationMenuPersonnel>
        get() = this._navigationPersonnel

//    private var _personnelIdAModifier= MutableLiveData<Long>()
//    val personnelIdAModifier : LiveData<Long>
//        get() = _personnelIdAModifier


    init{
        newPersonnel.value = Personnel()
        onBoutonClicked()

        navigationPersonnel.observeForever {
            Timber.i("NAVIGATION = $it")
        }
    }


    fun onClickBoutonAjoutPersonnel(){
        _navigationPersonnel.value = navigationMenuPersonnel.CREATION_PERSONNEL

    }

    fun onBoutonClicked() {
        _navigationPersonnel.value = navigationMenuPersonnel.EN_ATTENTE
    }

    fun onCheckedSwitchChefEquipeChanged( check: Boolean){

        newPersonnel.value?.fonction = check


        Timber.i("newPersonnel Fonction = $check")
        Timber.i("newPersonnel = ${newPersonnel.value?.fonction}")
    }


    fun onClickButtonCreationOrModificationEnded(){

        Timber.e("newPersonnel = ${newPersonnel.value?.prenom}")
        if(newPersonnel.value?.personnelId == null) sendNewPersonnelToDB()
        else updatePersonnelInDB()


        _navigationPersonnel.value = navigationMenuPersonnel.LISTE_PERSONNEL
    }

    private fun updatePersonnelInDB() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                var test = dataSource.updatePersonnel(newPersonnel.value!!)
            }
            newPersonnel.value = null
        }
    }


    private fun sendNewPersonnelToDB(){
        uiScope.launch {
            withContext(Dispatchers.IO) {
           var test = dataSource.insertPersonnel(newPersonnel.value!!)
            }
            newPersonnel.value = null

        }
    }

    fun onPersonnelClicked(id: Long){
        _navigationPersonnel.value = navigationMenuPersonnel.MODIFICATION_PERSONNEL
        retrievePersonnelData(id)

    }

    private fun retrievePersonnelData(id: Long){
        if(id != -1L){
            dataSource.getPersonnelById(id).observeForever{
                newPersonnel.value = it
                Timber.e("Valeur newPersonnel = ${newPersonnel.value}")
            }


        }
    }



    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }







}