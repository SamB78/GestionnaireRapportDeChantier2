package com.example.gestionnairerapportdechantier.vehicules.gestionVehicules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.VehiculeDao
import com.example.gestionnairerapportdechantier.entities.Vehicule
import kotlinx.coroutines.*
import timber.log.Timber
import java.time.LocalDate
import java.util.*

class GestionVehiculeViewModel(private val dataSource: VehiculeDao, id: Long = -1L) : ViewModel() {

    enum class gestionNavigation {
        ANNULATION,
        ENREGISTREMENT_VEHICULE,
        EN_ATTENTE,
        AJOUT_PHOTO,
        CHOIX_DATE_IMMAT
    }

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var vehicule = MutableLiveData<Vehicule>()
    var imageVehicule = MutableLiveData<String>()
    var date = MutableLiveData<LocalDate>()
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
                vehicule.value = getVehiculeValue(id)

                vehicule.value?.urlPictureVehicule?.let {
                    imageVehicule.value = it
                }
                vehicule.value?.miseEnCirculation?.let{
                    date.value = it
                }
            }
        } else {
            vehicule.value = Vehicule()
        }
    }

    private suspend fun getVehiculeValue(id: Long): Vehicule? {
        return withContext(Dispatchers.IO) {
            var vehicule = dataSource.getVehiculeById(id)
            vehicule
        }
    }

    fun onClickButtonCreationOrModificationEnded() {

        Timber.i("vehicule ready to save in DB = ${vehicule.value?.modele}")
        if (vehicule.value?.id == null) sendNewDataToDB()
        else updateDataInDB()

        _navigation.value = gestionNavigation.ENREGISTREMENT_VEHICULE
    }

    private fun sendNewDataToDB() {
        var vehiculeId: Long? = null
        uiScope.launch {
            Timber.i("vehicule : ${vehicule.value?.modele}")
            withContext(Dispatchers.IO) {
                vehiculeId = dataSource.insertVehicule(vehicule.value!!)
                Timber.i("VehiculeId  = $vehiculeId")
            }
        }
    }

    private fun updateDataInDB() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updateVehicule(vehicule.value!!)
            }
        }
    }

    fun onClickButtonAnnuler() {
        _navigation.value = gestionNavigation.ANNULATION
    }

    fun onBoutonClicked() {
        _navigation.value = gestionNavigation.EN_ATTENTE
    }

    fun onClickAjoutImage() {
        _navigation.value = gestionNavigation.AJOUT_PHOTO
    }

    fun ajoutPathImage(imagePath: String) {
        vehicule.value?.urlPictureVehicule = imagePath
        imageVehicule.value = imagePath
    }

    fun onClickButtonChoixDate() {
        _navigation.value = gestionNavigation.CHOIX_DATE_IMMAT
    }

    fun onClickDeletePicture(){
        vehicule.value?.urlPictureVehicule = null
        imageVehicule.value = null
    }

    fun onDateSelected(date: LocalDate){
        this.date.value = date
        vehicule.value?.miseEnCirculation = date
        Timber.i("Date enregistre miseEnCirculation : ${vehicule.value?.miseEnCirculation.toString()}")
    }

    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }


}