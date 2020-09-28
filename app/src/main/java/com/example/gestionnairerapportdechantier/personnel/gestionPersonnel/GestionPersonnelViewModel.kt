package com.example.gestionnairerapportdechantier.personnel.gestionPersonnel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.PersonnelDao
import com.example.gestionnairerapportdechantier.entities.Personnel
import kotlinx.coroutines.*
import timber.log.Timber

class GestionPersonnelViewModel(private val dataSource: PersonnelDao, id: Long = -1L) :
    ViewModel() {

    enum class gestionNavigation {
        ANNULATION,
        ENREGISTREMENT_PERSONNEL,
        EN_ATTENTE,
        AJOUT_PHOTO,
    }


    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var personnel = MutableLiveData<Personnel>()
    var imagePersonnel = MutableLiveData<String>()
    //navigation
    private var _navigation =
        MutableLiveData<gestionNavigation>()
    val navigation: LiveData<gestionNavigation>
        get() = this._navigation


    init {
        initializeData(id)
        onBoutonClicked()
    }


    private fun initializeData(id: Long) {
        if (id != -1L) {
            uiScope.launch {
                personnel.value = getPersonnelValue(id)

                personnel.value?.urlPicturepersonnel?.let{
                imagePersonnel.value = it
                }
            }
        } else {
            personnel.value = Personnel()
        }
    }

    private suspend fun getPersonnelValue(id: Long): Personnel? {
        return withContext(Dispatchers.IO) {
            var personnel = dataSource.getPersonnelById(id)
            personnel
        }
    }


    fun onBoutonClicked() {
        _navigation.value = gestionNavigation.EN_ATTENTE
    }


    //DELETE AND USE DATABINDING
    fun onCheckedSwitchChefEquipeChanged(check: Boolean) {
        personnel.value?.chefEquipe = check
        Timber.i("personnel = ${personnel.value?.chefEquipe}")

    }

    fun onCheckedSwitchInterimaireChanged(check: Boolean) {
        personnel.value?.interimaire = check
    }


    fun onClickButtonCreationOrModificationEnded() {

        Timber.i("personnel ready to save in DB = ${personnel.value?.prenom}")
        if (personnel.value?.personnelId == null) sendNewDataToDB()
        else updateDataInDB()

        _navigation.value = gestionNavigation.ENREGISTREMENT_PERSONNEL
    }


    private fun updateDataInDB() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updatePersonnel(personnel.value!!)
            }
        }
    }


    private fun sendNewDataToDB() {
        var personnelId: Long? = null
        uiScope.launch {
            withContext(Dispatchers.IO) {
                personnelId = dataSource.insertPersonnel(personnel.value!!)
                Timber.i("PersonnelId  = $personnelId")
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

    fun onClickAjoutImage(){
        _navigation.value = gestionNavigation.AJOUT_PHOTO
    }

    fun ajoutPathImage(imagePath: String) {

        personnel.value?.urlPicturepersonnel = imagePath
        imagePersonnel.value = imagePath
    }

    fun onClickDeletePicture(){
        personnel.value?.urlPicturepersonnel = null
        imagePersonnel.value = null
    }

}