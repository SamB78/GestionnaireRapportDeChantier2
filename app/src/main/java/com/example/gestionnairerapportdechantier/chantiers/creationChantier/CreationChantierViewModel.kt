package com.example.gestionnairerapportdechantier.chantiers.creationChantier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.Database.AssociationPersonnelChantierDao
import com.example.gestionnairerapportdechantier.Database.ChantierDao
import com.example.gestionnairerapportdechantier.Database.PersonnelDao
import com.example.gestionnairerapportdechantier.entities.Adresse
import com.example.gestionnairerapportdechantier.entities.AssociationPersonnelChantier
import com.example.gestionnairerapportdechantier.entities.Chantier
import com.example.gestionnairerapportdechantier.entities.Personnel
import kotlinx.coroutines.*
import timber.log.Timber

class CreationChantierViewModel(
    private val dataSourceChantier: ChantierDao,
    private val dataSourcePersonnel: PersonnelDao,
    private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao,
    id: Long = -1L
) :
    ViewModel() {

    enum class gestionNavigation {
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
    var chantier = MutableLiveData<Chantier>()
    var adresse = MutableLiveData<Adresse>()


    //Gestion Chefs de chantiers
    val listeChefsDeChantier = dataSourcePersonnel.getChefsdeChantier()
    private var _chefChantierSelectionne = MutableLiveData<Personnel>()
    val chefChantierSelectionne: LiveData<Personnel>
        get() = this._chefChantierSelectionne

    //Gestion du Personnel
    val listePersonnel = dataSourcePersonnel.getAllFromPersonnel()

    var _listePersonnelAAfficher = MutableLiveData<List<Personnel>>()
    val listePersonnelAAfficher: LiveData<List<Personnel>>
        get() = this._listePersonnelAAfficher

    //Liste personnel selectionné pour le chantier
    var _listePersonnelChantier =   mutableListOf<Personnel>()
    var _listePersonnelChantierValide = MutableLiveData<List<Personnel>>()
    val listePersonnelChantierValide: LiveData<List<Personnel>>
        get() = this._listePersonnelChantierValide


    //list Association Personnel - chantier

    var associationPersonnelChantier = AssociationPersonnelChantier(0,0)


    //Image Chantier
    var imageChantier = MutableLiveData<String>()



    //Navigation
    private var _navigation = MutableLiveData<gestionNavigation>()
    val navigation: LiveData<gestionNavigation>
        get() = _navigation

    init {
        initializeData(id)
        Timber.i("Chantier initialisé  = ${chantier.value?.numeroChantier}")
        onBoutonClicked()
        initializeDataPersonnel()

    }

    private fun initializeDataPersonnel() {
        uiScope.launch {
            _listePersonnelAAfficher.value = getPersonnelAAfficher()
        }
    }

    private suspend fun getPersonnelAAfficher(): List<Personnel> {
        return withContext(Dispatchers.IO) {
            var listePersonnel = dataSourcePersonnel.getAllFromPersonnel2()
            listePersonnel
        }
    }

    private fun initializeData(id: Long) {
        if (id != -1L) {
            uiScope.launch {
                chantier.value = getChantierValue(id)
                adresse.value = chantier.value!!.adresseChantier
            }
        } else {
            chantier.value = Chantier()
            adresse.value = Adresse()
        }

    }


    private suspend fun getChantierValue(id: Long): Chantier? {
        return withContext(Dispatchers.IO) {
            var chantier = dataSourceChantier.getChantierById(id)
            chantier
        }
    }

    fun onBoutonClicked() {
        _navigation.value = gestionNavigation.EN_ATTENTE
    }

    fun onClickButtonPassageEtape2() {
        _navigation.value = gestionNavigation.PASSAGE_ETAPE2
    }

    fun onClickButtonCreationOrModificationEnded() {

        chantier.value?.adresseChantier = adresse.value!!
        Timber.i("Chantier ready to save in DB = ${chantier.value?.nomChantier}")
        if (chantier.value?.chantierId == null) sendNewDataToDB()
        else updateDataInDB()

        _navigation.value = gestionNavigation.ENREGISTREMENT_CHANTIER
    }

    private fun updateDataInDB() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceChantier.update(chantier.value!!)
            }
        }
    }

    private fun sendNewDataToDB() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
               val chantierId =  dataSourceChantier.insert(chantier.value!!)
                listePersonnelChantierValide.value?.forEach{
                    associationPersonnelChantier = AssociationPersonnelChantier(it.personnelId!!, chantierId.toInt())
                    dataSourceAssociationPersonnelChantier.insertAssociationPersonnelChantier(associationPersonnelChantier)
                }
            }
        }
    }


    fun onClickChefChantier(id: Long) {

        Timber.i("Passage dans onClickChefChantier valeur id : $id")

        listeChefsDeChantier.value!!.forEach { event ->
            if (event.personnelId!! == id.toInt()) {
                _chefChantierSelectionne.value = event
            }
        }
        //_chefChantierSelectionne.value = listeChefsDeChantier.value!!.first { it.personnelId!!.equals(id) }
    }

    fun onClickChefChantierValide() {
        chantier.value?.chefChantierId = _chefChantierSelectionne.value!!.personnelId
        _navigation.value = gestionNavigation.PASSAGE_ETAPE3
    }

    fun onSelectionPersonnel(id: Long) {
        Timber.i("PASSAGE DANS onSelectionPersonnel id = $id ")
        _listePersonnelAAfficher.value!!.forEach {
            if (it.personnelId!! == id.toInt()) {
                if (it.isChecked) {
                    it.isChecked = false
                    _listePersonnelChantier.remove(it)


                } else {
                    it.isChecked = true
                   _listePersonnelChantier.add(it)
                    Timber.i("Personnel = $it")

                }
            }
        }

        _listePersonnelChantier.forEach {
            Timber.i("Element _listePersonnelChantier = $it ")
        }
        _listePersonnelAAfficher.value = _listePersonnelAAfficher.value
    }



    fun onClickChoixEquipeValide(){
        _listePersonnelChantierValide.value = _listePersonnelChantier

        Timber.i("Element _listePersonnelChantierValide dans le Timber suivant ")
        _listePersonnelChantier.forEach{
            Timber.i("Element _listePersonnelChantierValide = $it ")
        }
        _navigation.value = gestionNavigation.CONFIRMATION_ETAPE3

    }

    fun onClickConfirmationChoixEquipe() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun onClickButtonAnnuler() {
        _navigation.value = gestionNavigation.ANNULATION
    }

    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }

    fun onClickChoixEquipeConfirmationValidation() {
        _navigation.value = gestionNavigation.PASSAGE_ETAPE4
    }

    fun onClickAjoutImage(){
        _navigation.value = gestionNavigation.AJOUT_IMAGE
    }

    fun ajoutPathImage(imagePath: String){
        chantier.value!!.urlPictureChantier = imagePath
        imageChantier.value = imagePath
        Timber.i("imagePath: $imagePath, chantier.value.url: ${chantier.value?.urlPictureChantier}")
    }

    fun onClickConfirmationEtapeImage(){
        _navigation.value = gestionNavigation.PASSAGE_ETAPE_RESUME
    }

    fun onClickButtonModifier(){
        _navigation.value = gestionNavigation.MODIFICATION
    }

    fun onClickDeletePicture(){
        chantier.value?.urlPictureChantier = null
        imageChantier?.value = null
    }
}

