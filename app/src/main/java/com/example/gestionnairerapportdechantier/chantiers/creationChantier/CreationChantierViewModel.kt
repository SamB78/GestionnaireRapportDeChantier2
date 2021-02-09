package com.example.gestionnairerapportdechantier.chantiers.creationChantier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.AssociationPersonnelChantierDao
import com.example.gestionnairerapportdechantier.database.ChantierDao
import com.example.gestionnairerapportdechantier.database.PersonnelDao
import com.example.gestionnairerapportdechantier.entities.AssociationPersonnelChantier
import com.example.gestionnairerapportdechantier.entities.Chantier
import com.example.gestionnairerapportdechantier.entities.Personnel
import kotlinx.coroutines.*
import timber.log.Timber

class CreationChantierViewModel(
    private val dataSourceChantier: ChantierDao,
    private val dataSourcePersonnel: PersonnelDao,
    private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao,
    val id: Long = -1L
) :
    ViewModel() {

    enum class GestionNavigation {
        ANNULATION,
        PASSAGE_ETAPE2,
        PASSAGE_ETAPE3,
        CONFIRMATION_ETAPE3,
        PASSAGE_ETAPE4,
        AJOUT_IMAGE,
        PASSAGE_ETAPE_RESUME,
        ENREGISTREMENT_CHANTIER,
        MODIFICATION,
        EN_ATTENTE
    }


    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //Création du chantier avec son adresse
    var chantier = MutableLiveData<Chantier>(Chantier())

//
//    val listePersonnel = dataSourcePersonnel.getAllFromPersonnel()

    //Gestion Chefs de chantiers
    private var _listeChefsDeChantier = MutableLiveData<MutableList<Personnel>>(mutableListOf())
    val listeChefsDeChantier: LiveData<MutableList<Personnel>>
        get() = this._listeChefsDeChantier

    //    val listeChefsDeChantier = dataSourcePersonnel.getChefsdeChantier()
    private var _chefChantierSelectionne = MutableLiveData<Personnel>(Personnel())
    val chefChantierSelectionne: LiveData<Personnel>
        get() = this._chefChantierSelectionne


    //Liste du Personnel
    private var _listePersonnel = MutableLiveData<List<Personnel>>(mutableListOf())
    val listePersonnel: LiveData<List<Personnel>>
        get() = this._listePersonnel

    //Liste personnel selectionné pour le chantier
    private var _listePersonnelChantierValide = MutableLiveData<List<Personnel>>()
    val listePersonnelChantierValide: LiveData<List<Personnel>>
        get() = this._listePersonnelChantierValide


    //list Association Personnel - chantier

    private var _listAssociationsPersonnelChantier =
        MutableLiveData<MutableList<AssociationPersonnelChantier>>(
            mutableListOf()
        )
    val listAssociationPersonnelChantier: LiveData<MutableList<AssociationPersonnelChantier>>
        get() = this._listAssociationsPersonnelChantier


    //Image Chantier
    var imageChantier = MutableLiveData<String>()


    //Navigation
    private var _navigation = MutableLiveData<GestionNavigation>()
    val navigation: LiveData<GestionNavigation>
        get() = _navigation

    init {
        initializeListsPersonnel()
        initializeData(id)
        onBoutonClicked()


    }

    private fun initializeListsPersonnel() {
        uiScope.launch {
            _listePersonnel.value = getPersonnelAAfficher()

            _listePersonnel.value!!.forEach {
                if (it.chefEquipe) {
                    val chef = it.copy()
                    _listeChefsDeChantier.value?.add(chef)
                }
            }

        }
    }

    private suspend fun getPersonnelAAfficher(): List<Personnel> {
        return withContext(Dispatchers.IO) {
            dataSourcePersonnel.getAllFromPersonnel2()
        }
    }

    private fun initializeData(id: Long) {
        if (id != -1L) {
            uiScope.launch {
                chantier.value = getChantierValue(id)
                _chefChantierSelectionne.value =
                    getChefChantier(chantier.value!!.chefChantierId!!.toLong())
                _listeChefsDeChantier.value!!.find { it.personnelId == chefChantierSelectionne.value!!.personnelId }?.isChecked =
                    true
                _listAssociationsPersonnelChantier.value = loadAssociationsChantierPersonnel(id)  as MutableList<AssociationPersonnelChantier>
                _listAssociationsPersonnelChantier.value?.forEach {  associationPersonnelChantier ->
                    _listePersonnel.value?.find { it.personnelId == associationPersonnelChantier.personnelID }?.isChecked = true
                }
                imageChantier.value = chantier.value!!.urlPictureChantier


            }
        } else {
            chantier.value = Chantier()
        }

    }


    private suspend fun getChantierValue(id: Long): Chantier {
        return withContext(Dispatchers.IO) {
            dataSourceChantier.getChantierById(id)
        }
    }

    private suspend fun getChefChantier(id: Long): Personnel {
        return withContext(Dispatchers.IO) {
            dataSourcePersonnel.getPersonnelById(id)
        }
    }

    private suspend fun loadAssociationsChantierPersonnel(id: Long): List<AssociationPersonnelChantier> {
        return withContext(Dispatchers.IO) {
            dataSourceAssociationPersonnelChantier.getAssociationPersonnelChantierByChantierId(id.toInt())
        }
    }

    fun onBoutonClicked() {
        _navigation.value = GestionNavigation.EN_ATTENTE
    }

    fun onClickButtonPassageEtape2() {
        _navigation.value = GestionNavigation.PASSAGE_ETAPE2
    }

    fun onClickButtonCreationOrModificationEnded() {

        uiScope.launch {
            Timber.i("Chantier ready to save in DB = ${chantier.value?.nomChantier}, ${chantier.value?.adresseChantier?.adresseToString()}")
            if (chantier.value?.chantierId == null) {
                Timber.i("sendNewDataToDB()")
                sendNewDataToDB()
            } else updateDataInDB()

            _navigation.value = GestionNavigation.ENREGISTREMENT_CHANTIER
        }
    }

    private suspend fun updateDataInDB() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceChantier.update(chantier.value!!)

                dataSourceAssociationPersonnelChantier.deleteAssociationPersonnelChantierIdByChantierId(chantier.value!!.chantierId!!)

                listePersonnelChantierValide.value?.forEach {
                    dataSourceAssociationPersonnelChantier.insertAssociationPersonnelChantier(
                        AssociationPersonnelChantier(it.personnelId!!, chantier.value!!.chantierId!!)
                    )
                }



            }
        }.join()
    }

    private suspend fun sendNewDataToDB() {
        Timber.i("sendNewDataToDB()")
        var chantierId: Long
        uiScope.launch {
            withContext(Dispatchers.IO) {

                chantierId = dataSourceChantier.insert(chantier.value!!)

                listePersonnelChantierValide.value?.forEach {
                    dataSourceAssociationPersonnelChantier.insertAssociationPersonnelChantier(
                        AssociationPersonnelChantier(it.personnelId!!, chantierId.toInt())
                    )
                }
            }
        }.join()
    }


    fun onClickChefChantier(id: Long) {

        Timber.i("Passage dans onClickChefChantier valeur id : $id")

        listeChefsDeChantier.value!!.forEach { event ->
            if (event.personnelId!! == id.toInt()) {
                _chefChantierSelectionne.value = event.copy(isChecked = false)
                event.isChecked = true
            }
            else{
                event.isChecked = false
            }
        }
        //_chefChantierSelectionne.value = listeChefsDeChantier.value!!.first { it.personnelId!!.equals(id) }
    }

    fun onClickChefChantierValide() {
        chantier.value?.chefChantierId = _chefChantierSelectionne.value!!.personnelId
        _navigation.value = GestionNavigation.PASSAGE_ETAPE3
    }

    fun onSelectionPersonnel(id: Long) {
        Timber.i("PASSAGE DANS onSelectionPersonnel id = $id ")
        _listePersonnel.value!!.forEach {
            if (it.personnelId!! == id.toInt()) {
                if (it.isChecked) {
                    it.isChecked = false
                    // listePersonnelChantier.remove(it)


                } else {
                    it.isChecked = true
                    //listePersonnelChantier.add(it)
                    Timber.i("Personnel = $it")

                }
            }
        }

        _listePersonnel.value = _listePersonnel.value
    }


    fun onClickChoixEquipeValide() {
        val listePersonnelChantier = mutableListOf<Personnel>()
        _listePersonnel.value!!.forEach {
            if (it.isChecked) {
                listePersonnelChantier.add(it.copy(isChecked = false))
            }
        }
        _listePersonnelChantierValide.value = listePersonnelChantier

        _navigation.value = GestionNavigation.CONFIRMATION_ETAPE3

    }

    fun onClickConfirmationChoixEquipe() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun onClickButtonAnnuler() {
        _navigation.value = GestionNavigation.ANNULATION
    }

    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }

    fun onClickChoixEquipeConfirmationValidation() {
        _navigation.value = GestionNavigation.PASSAGE_ETAPE4
    }

    fun onClickAjoutImage() {
        _navigation.value = GestionNavigation.AJOUT_IMAGE
    }

    fun ajoutPathImage(imagePath: String) {
        chantier.value!!.urlPictureChantier = imagePath
        imageChantier.value = imagePath
        Timber.i("imagePath: $imagePath, chantier.value.url: ${chantier.value?.urlPictureChantier}")
    }

    fun onClickConfirmationEtapeImage() {
//        sendNewDataToDB()
        _navigation.value = GestionNavigation.PASSAGE_ETAPE_RESUME
    }

    fun onClickButtonModifier() {
        _navigation.value = GestionNavigation.MODIFICATION
    }

    fun onClickDeletePicture() {
        chantier.value?.urlPictureChantier = null
        imageChantier?.value = null
    }
}

