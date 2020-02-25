package com.example.gestionnairerapportdechantier.gestionPersonnel

import android.util.Log
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

    init{
        newPersonnel.value = Personnel()
        onBoutonClicked()
    }


    fun onClickBoutonAjoutPersonnel(){
        _navigationPersonnel.value = navigationMenuPersonnel.CREATION_PERSONNEL
    }

    fun onBoutonClicked() {
        _navigationPersonnel.value = navigationMenuPersonnel.EN_ATTENTE
    }

    fun onClickBoutonCreationTermine(){

        Timber.e("newPersonnel = ${newPersonnel.value?.prenom}")
        sendNewPersonnelToDB()
        _navigationPersonnel.value = navigationMenuPersonnel.LISTE_PERSONNEL
    }


    fun onCheckedSwitchChefEquipeChanged( check: Boolean){
        if(check){
            newPersonnel.value?.fonction = 1
        }else{
            newPersonnel.value?.fonction = 0
        }

        Timber.i("newPersonnel Fonction = $check")
        Timber.i("newPersonnel = ${newPersonnel.value?.fonction}")
    }


    fun sendNewPersonnelToDB(){
        uiScope.launch {
            withContext(Dispatchers.IO) {
           var test = dataSource.insertPersonnel(newPersonnel.value!!)
//                newPersonnel.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }







}