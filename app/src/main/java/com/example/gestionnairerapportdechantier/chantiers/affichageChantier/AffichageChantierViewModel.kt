package com.example.gestionnairerapportdechantier.chantiers.affichageChantier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.AssociationPersonnelChantierDao
import com.example.gestionnairerapportdechantier.database.ChantierDao
import com.example.gestionnairerapportdechantier.database.PersonnelDao
import com.example.gestionnairerapportdechantier.database.RapportChantierDao
import com.example.gestionnairerapportdechantier.entities.Adresse
import com.example.gestionnairerapportdechantier.entities.Chantier
import com.example.gestionnairerapportdechantier.entities.Personnel
import com.example.gestionnairerapportdechantier.entities.RapportChantier
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class AffichageChantierViewModel(
    private val dataSourceChantier: ChantierDao,
    private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao,
    private val dataSourcePersonnel: PersonnelDao,
    private val dataSourceRapporChantier: RapportChantierDao,
    val idChantier: Long = -1
) : ViewModel() {

    enum class navigationMenu {
        CREATION,
        MODIFICATION_RAPPORT_CHANTIER,
        CONSULTATION,
        AJOUT,
        SELECTION_DATE,
        EN_ATTENTE,
        EDIT,
        EXPORT,
        SELECTION_DATE_EXPORT,
    }

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //Création du chantier avec son adresse
    var chantier = MutableLiveData<Chantier>()
    var adresse = MutableLiveData<Adresse>()

    //Chef de Chantier
    private var _chefChantierSelectionne = MutableLiveData<Personnel>(Personnel())
    val chefChantierSelectionne: LiveData<Personnel>
        get() = this._chefChantierSelectionne

//    //Liste Rapports de chantiers
//    private var _listeRapportsDeChantiers = MutableLiveData<List<RapportChantier>>()
//    val listeRapportChantier: LiveData<List<RapportChantier>>
//        get() = this._listeRapportsDeChantiers


    //Liste personnel selectionné pour le chantier
    var _listePersonnelChantier = mutableListOf<Personnel>()
    var _listePersonnelChantierValide = MutableLiveData<List<Personnel>>()
    val listePersonnelChantierValide: LiveData<List<Personnel>>
        get() = this._listePersonnelChantierValide

    var _listeRapportsChantiers = MutableLiveData<List<RapportChantier>>(emptyList())
    val listeRapportsChantiers: LiveData<List<RapportChantier>>
        get() = this._listeRapportsChantiers

    private var _navigation = MutableLiveData<navigationMenu>()
    val navigation: LiveData<navigationMenu>
        get() = this._navigation

    private var _idRapportChantier = MutableLiveData<Long>()
    val idRapportChantier: LiveData<Long>
        get() = this._idRapportChantier

    // Dates pour export

    private var _dateDebut = MutableLiveData<Long>()
    val dateDebut: LiveData<Long>
        get() = this._dateDebut

    private var _dateFin = MutableLiveData<Long>()
    val dateFin: LiveData<Long>
        get() = this._dateFin


    init {
//        RetrieveRapportChantiers(idChantier)
//        initializeData(idChantier)
        onResumeGestionMaterielFragment()
        Timber.i("Chantier initialisé  = ${chantier.value?.numeroChantier}")
        onBoutonClicked()
    }

    private fun initializeData(id: Long) {
        if (id != -1L) {
            uiScope.launch {
                chantier.value = getChantierValue(id)
                adresse.value = chantier.value!!.adresseChantier
                _chefChantierSelectionne.value =
                    getChefChantierValue(chantier.value!!.chefChantierId)
                _listePersonnelChantierValide.value = initializeDataPersonnel()
            }
        } else {
            chantier.value = Chantier()
            adresse.value = Adresse()
        }

    }

    private fun RetrieveRapportChantiers(idChantier: Long) {
        uiScope.launch {
            _listeRapportsChantiers.value = withContext(Dispatchers.IO) {
                dataSourceRapporChantier.getAllFromRapportChantierByChantierId(idChantier)
                    .sortedByDescending { it.dateRapportChantier }
            }
        }
    }

    private suspend fun getChefChantierValue(chefChantierId: Int?): Personnel {
        return withContext(Dispatchers.IO) {
            var chefChantier = dataSourcePersonnel.getPersonnelById(chefChantierId!!.toLong())
            chefChantier
        }
    }

    private suspend fun getChantierValue(id: Long): Chantier? {
        return withContext(Dispatchers.IO) {
            var chantier = dataSourceChantier.getChantierById(id)
            chantier
        }
    }

    private suspend fun initializeDataPersonnel(): List<Personnel>? {
        return withContext(Dispatchers.IO) {
            Timber.i("Entrée initializeDataPersonnel")
            var listeAssociation = chantier.value?.chantierId?.let {
                dataSourceAssociationPersonnelChantier.getAssociationPersonnelChantierIdByChantierId(
                    it
                )
            }
            Timber.i(" listeAssociation: Affichage")
            listeAssociation?.forEach {
                Timber.i("listeAssociation =  $it")
            }
            var listePersonnel =
                listeAssociation?.let { dataSourcePersonnel.getPersonnelsByIds(it) }
            listePersonnel
        }
    }

    fun onClickBoutonAjoutRapportChantier() {
        _navigation.value = navigationMenu.SELECTION_DATE
    }

    fun onDateSelected() {
        _navigation.value = navigationMenu.CREATION
    }

    fun onButtonClickEditRapportChantier(rapportChantier: RapportChantier) {
        _idRapportChantier.value = rapportChantier.rapportChantierId!!.toLong()
        _navigation.value = navigationMenu.MODIFICATION_RAPPORT_CHANTIER
    }

    fun onButtonClickConsultRapportChantier(rapportChantier: RapportChantier) {
        _idRapportChantier.value = rapportChantier.rapportChantierId!!.toLong()
        _navigation.value = navigationMenu.CONSULTATION
    }

    var needToActualizeData= false
    fun onClickButtonEditChantier() {
        _navigation.value = navigationMenu.EDIT
        needToActualizeData = true
    }

    fun onResumeAfterEditChantier(){
        if(needToActualizeData){
            onResumeGestionMaterielFragment()
            needToActualizeData = false
        }

    }


    fun onClickButtonExportData() {

        _navigation.value = navigationMenu.SELECTION_DATE_EXPORT
    }

    fun onDatesToExportSelected(date1: Long, date2: Long) {
        _dateDebut.value = date1
        _dateFin.value = date2

        Timber.i("Dates = ${dateDebut.value} ${dateFin.value}")

        _navigation.value = navigationMenu.EXPORT
    }

    fun onBoutonClicked() {
        _navigation.value = navigationMenu.EN_ATTENTE
    }

    fun onResumeGestionMaterielFragment() {
        RetrieveRapportChantiers(idChantier)
        initializeData(idChantier)
    }


    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
