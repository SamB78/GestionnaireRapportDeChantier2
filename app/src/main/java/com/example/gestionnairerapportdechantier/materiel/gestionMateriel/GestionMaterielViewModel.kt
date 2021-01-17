package com.example.gestionnairerapportdechantier.materiel.gestionMateriel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.MaterielDao
import com.example.gestionnairerapportdechantier.entities.Materiel
import kotlinx.coroutines.*
import timber.log.Timber
import java.time.LocalDate
import java.util.*

class GestionMaterielViewModel(private val dataSource: MaterielDao, id: Long = -1L) : ViewModel() {

    enum class gestionNavigation {
        ANNULATION,
        ENREGISTREMENT_MATERIEL,
        EN_ATTENTE,
        AJOUT_PHOTO,
        CHOIX_DATE_IMMAT
    }

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var materiel = MutableLiveData<Materiel>()
    var imageMateriel = MutableLiveData<String>()
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
                materiel.value = getMaterielValue(id)

                materiel.value?.urlPictureMateriel?.let {
                    imageMateriel.value = it
                }
                materiel.value?.miseEnCirculation?.let{
                    date.value = it
                }
            }
        } else {
            materiel.value = Materiel()
        }
    }

    private suspend fun getMaterielValue(id: Long): Materiel? {
        return withContext(Dispatchers.IO) {
            var materiel = dataSource.getMaterielById(id)
            materiel
        }
    }

    fun onClickButtonCreationOrModificationEnded() {

        Timber.i("materiel ready to save in DB = ${materiel.value?.modele}")
        if (materiel.value?.id == null) sendNewDataToDB()
        else updateDataInDB()

        _navigation.value = gestionNavigation.ENREGISTREMENT_MATERIEL
    }

    private fun sendNewDataToDB() {
        var materielId: Long? = null
        uiScope.launch {
            Timber.i("materiel : ${materiel.value?.modele}")
            withContext(Dispatchers.IO) {
                materielId = dataSource.insertMateriel(materiel.value!!)
                Timber.i("MaterielId  = $materielId")
            }
        }
    }

    private fun updateDataInDB() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updateMateriel(materiel.value!!)
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
        materiel.value?.urlPictureMateriel = imagePath
        imageMateriel.value = imagePath
    }

    fun onClickButtonChoixDate() {
        _navigation.value = gestionNavigation.CHOIX_DATE_IMMAT
    }

    fun onClickDeletePicture(){
        materiel.value?.urlPictureMateriel = null
        imageMateriel.value = null
    }

    fun onDateSelected(date: LocalDate){
        this.date.value = date
        materiel.value?.miseEnCirculation = date
        Timber.i("Date enregistre miseEnCirculation : ${materiel.value?.miseEnCirculation.toString()}")
    }

    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }
}