package com.example.gestionnairerapportdechantier.rapportChantier.affichageRapportChantier

import android.app.Application
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.database.*
import com.example.gestionnairerapportdechantier.entities.*
import kotlinx.coroutines.*
import org.apache.poi.ss.usermodel.WorkbookFactory
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

class AffichageDetailsRapportChantierViewModel(
    application: Application,
    private val dataSourceRapportChantier: RapportChantierDao,
    private val dataSourceChantier: ChantierDao,
    private val dataSourcePersonnel: PersonnelDao,
    private val dataSourceMateriel: MaterielDao,
    private val dataSourceMaterielLocation: MaterielLocationDao,
    private val dataSourceMateriaux: MateriauxDao,
    private val dataSourceSousTraitance: SousTraitanceDao,
    private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao,
    private val dataSourceAssociationPersonnelRapportChantierDao: AssociationPersonnelRapportChantierDao,
    private val dataSourceAssociationMaterielRapportChantierDao: AssociationMaterielRapportChantierDao,
    private val dataSourceAssociationMaterielLocationRapportChantierDao: AssociationMaterielLocationRapportChantierDao,
    private val dataSourceAssociationMateriauxRapportChantierDao: AssociationMateriauxRapportChantierDao,
    private val dataSourceAssociationSousTraitanceRapportChantierDao: AssociationSousTraitanceRapportChantierDao,
    rapportChantierId: Long = -1L,
    val chantierId: Int = -1,
    dateBeginning: Long = -1L,
    dateEnd: Long = -1L
) : AndroidViewModel(application) {

    enum class GestionNavigation {
        RETOUR,
        CREATION_EXCEL,

        SELECTION_CHANTIER,
        SELECTION_DATE,
        DONNEES_SELECTIONNEES,
        EN_ATTENTE,
    }

    data class Tableau(val value: String = "")

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //navigation
    private var _navigation = MutableLiveData<GestionNavigation>()
    val navigation: LiveData<GestionNavigation>
        get() = this._navigation

    var listeChantiers = MutableLiveData<List<Chantier>>(emptyList())

    var selectedChantier = MutableLiveData<Chantier>()

    private var _listRapportsChantier = MutableLiveData<List<RapportChantier>>(emptyList())
    val listRapportsChantier: LiveData<List<RapportChantier>>
        get() = _listRapportsChantier


    //ASSOCIATIONS
    private var listeAssociationsPersonnelRapportChantier =
        MutableLiveData<List<AssociationPersonnelRapportChantier>>(emptyList())

    private var listeAssociationsMaterielRapportChantier =
        MutableLiveData<List<AssociationMaterielRapportChantier>>(emptyList())

    private var listeAssociationsMaterielLocationRapportChantier =
        MutableLiveData<List<AssociationMaterielLocationRapportChantier>>(emptyList())

    private var listeAssociationsMateriauxRapportChantier =
        MutableLiveData<List<AssociationMateriauxRapportChantier>>(emptyList())

    private var listeAssociationsSousTraitanceRapportChantier =
        MutableLiveData<List<AssociationSousTraitanceRapportChantier>>(emptyList())


    //CLASSES
    private var _listePersonnelRapportChantier = MutableLiveData<List<Personnel>>(emptyList())
    val listPersonnelRapportChantier: LiveData<List<Personnel>>
        get() = _listePersonnelRapportChantier

    private var _listeMaterielRapportChantier = MutableLiveData<List<Materiel>>(emptyList())
    val listeMaterielRapportChantier: LiveData<List<Materiel>>
        get() = _listeMaterielRapportChantier

    private var _listeMaterielLocationRapportChantier =
        MutableLiveData<List<MaterielLocation>>(emptyList())
    val listeMaterielLocationRapportChantier: LiveData<List<MaterielLocation>>
        get() = _listeMaterielLocationRapportChantier

    private var _listeMateriauxRapportChantier = MutableLiveData<List<Materiaux>>(emptyList())
    val listeMateriauxRapportChantier: LiveData<List<Materiaux>>
        get() = _listeMateriauxRapportChantier

    private var _listeSousTraitanceRapportChantier =
        MutableLiveData<List<SousTraitance>>(emptyList())
    val listeSousTraitanceRapportChantier: LiveData<List<SousTraitance>>
        get() = _listeSousTraitanceRapportChantier


    // Listes Personnel pour tableau
    private var _tableauPersonnel = MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val tableauPersonnel: LiveData<List<Tableau>>
        get() = this._tableauPersonnel

    private var _listeNames = MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val listeNames: LiveData<List<Tableau>>
        get() = this._listeNames

    private var _listeTotalNbHeures = MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val listeTotal: LiveData<List<Tableau>>
        get() = this._listeTotalNbHeures

    // Listes Materiel pour tableau
    private var _tableauMateriel = MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val tableauMateriel: LiveData<List<Tableau>>
        get() = this._tableauMateriel

    private var _listeNomsMateriel = MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val listeNomsMateriel: LiveData<List<Tableau>>
        get() = this._listeNomsMateriel

    private var _listeTotalNbHeuresMateriel =
        MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val listeTotalMateriel: LiveData<List<Tableau>>
        get() = this._listeTotalNbHeuresMateriel

    // Listes MaterielLocation pour tableau
    private var _tableauMaterielLocation = MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val tableauMaterielLocation: LiveData<List<Tableau>>
        get() = this._tableauMaterielLocation

    private var _listeNomsMaterielLocation = MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val listeNomsMaterielLocation: LiveData<List<Tableau>>
        get() = this._listeNomsMaterielLocation

    private var _listeTotalNbHeuresMaterielLocation =
        MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val listeTotalMaterielLocation: LiveData<List<Tableau>>
        get() = this._listeTotalNbHeuresMaterielLocation

    // Listes Materiaux pour tableau

    private var _tableauMateriaux = MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val tableauMateriaux: LiveData<List<Tableau>>
        get() = this._tableauMateriaux

    private var _listeNomsMateriaux = MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val listeNomsMateriaux: LiveData<List<Tableau>>
        get() = this._listeNomsMateriaux

    private var _listeTotalNbHeuresMateriaux =
        MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val listeTotalMateriaux: LiveData<List<Tableau>>
        get() = this._listeTotalNbHeuresMateriaux

    // Listes SousTraitance pour tableau

    private var _tableauSousTraitance = MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val tableauSousTraitance: LiveData<List<Tableau>>
        get() = this._tableauSousTraitance

    private var _listeNomsSousTraitance = MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val listeNomsSousTraitance: LiveData<List<Tableau>>
        get() = this._listeNomsSousTraitance

    private var _listeTotalNbHeuresSousTraitance =
        MutableLiveData<List<Tableau>>(listOf(Tableau("test")))
    val listeTotalSousTraitance: LiveData<List<Tableau>>
        get() = this._listeTotalNbHeuresSousTraitance

    private val totalDates: MutableList<LocalDate> = ArrayList()


    private var _numberOfColumns = MutableLiveData<Int>(1)
    val numberOfColumns: LiveData<Int>
        get() = this._numberOfColumns


    var showTabHeuresTravaillees = MutableLiveData<Boolean>(false)
    var showTabMateriel = MutableLiveData<Boolean>(false)
    var showTabMateriaux = MutableLiveData<Boolean>(false)

    init {
        loadChantiers()
        if (dateBeginning != -1L && dateEnd != -1L) {
            uiScope.launch {
                loadChantierFromId(chantierId)
                loadDataFromDates(dateBeginning, dateEnd)
            }
        } else {
            loadDataFromId(rapportChantierId)
        }
    }

    private fun generateListIdsRapportsChantier(listRapportChantiersIds: List<RapportChantier>?): List<Long> {
        val list: MutableList<Long> = mutableListOf()
        listRapportChantiersIds?.forEach {
            list.add(it.rapportChantierId!!.toLong())
        }
        return list
    }

    private fun loadChantiers() {

        uiScope.launch {
            withContext(Dispatchers.IO) {
                listeChantiers.postValue(dataSourceChantier.getAllFromChantier2())
            }
        }
    }
    ///// LOAD DATA FROM ID /////

    private fun loadDataFromId(rapportChantierId: Long) {
        uiScope.launch {
            _listRapportsChantier.postValue(
                dataSourceRapportChantier.getRapportChantierById(rapportChantierId) as List<RapportChantier>
            )
            val listRapportsChantierIds =
                generateListIdsRapportsChantier(_listRapportsChantier.value!!)

            loadDataPersonnel(listRapportsChantierIds)
            loadDataMateriel(listRapportsChantierIds)
            loadDataMaterielLocation(listRapportsChantierIds)
            loadDataMateriaux(listRapportsChantierIds)
            loadDataSousTraitance(listRapportsChantierIds)
        }

    }

    ///// LOAD DATA FROM DATES /////
    lateinit var uri: Uri

    private fun loadDataFromDates(date1: Long, date2: Long) {

        var dateBeginning = Instant.ofEpochMilli(date1).atZone(ZoneId.systemDefault()).toLocalDate()
        val dateEnd = Instant.ofEpochMilli(date2).atZone(ZoneId.systemDefault()).toLocalDate()
        lateinit var listRapportsChantierIds: List<Long>

        while (!dateBeginning.isAfter(dateEnd)) {
            totalDates.add(dateBeginning)
            dateBeginning = dateBeginning.plusDays(1)
        }
        totalDates.forEach {
            Timber.i("loadDataFromDates = $it")
        }
        uiScope.launch {
            _listRapportsChantier.value = loadRapportsChantiersFromDates(
                totalDates,
                chantierId
            )

            if (listRapportsChantier.value?.count()!! > 0) {
                _numberOfColumns.value = listRapportsChantier.value?.count()
            }
            listRapportsChantierIds = generateListIdsRapportsChantier(_listRapportsChantier.value)

            loadDataPersonnel(listRapportsChantierIds)
            loadDataMateriel(listRapportsChantierIds)
            loadDataMaterielLocation(listRapportsChantierIds)
            loadDataMateriaux(listRapportsChantierIds)
            loadDataSousTraitance(listRapportsChantierIds)
            creationListeNombreHeuresTravaillees()
            creationTableauMateriel()
            creationTableauMateriaux()
            uri = onClickGenerateXlsxFile()

        }
    }

    private suspend fun loadChantierFromId(chantierId: Int){
        uiScope.launch {
            selectedChantier.value = withContext(Dispatchers.IO) {
                dataSourceChantier.getChantierById(chantierId.toLong())
            }
            Timber.i("Numéro chantier = $chantierId")
            Timber.i("selectedChantier =  ${selectedChantier.value!!.numeroChantier!!}")
        }.join()
    }

    private suspend fun loadRapportsChantiersFromDates(
        liste: List<LocalDate>,
        chantierId: Int
    ): List<RapportChantier> {

        return withContext(Dispatchers.IO) {
            dataSourceRapportChantier.getRapportsChantiersFromDatesList(liste, chantierId.toLong())
                .sortedBy { it.dateRapportChantier }
        }
    }


    ///// LOAD DATA PERSONNEL /////

    private suspend fun loadDataPersonnel(listRapportChantiersIds: List<Long>) {

        uiScope.launch {

            listeAssociationsPersonnelRapportChantier.value =
                loadListAssociationPersonnelRapportChantier(listRapportChantiersIds)


            _listePersonnelRapportChantier.value = initializeListPersonnel(
                listeAssociationsPersonnelRapportChantier.value!!
            )

        }.join()
    }

    private suspend fun loadListAssociationPersonnelRapportChantier(listRapportChantiersIds: List<Long>): List<AssociationPersonnelRapportChantier> {
        return withContext(Dispatchers.IO) {
            val listAssociation =
                dataSourceAssociationPersonnelRapportChantierDao.getAssociationsPersonnelRapportChantierByRapportsChantierIdsList(
                    listRapportChantiersIds
                )
            Timber.i("listAssociation")
            listAssociation.forEach {
                Timber.i("withContext listAssociation = ${it.associationId}")
            }
            listAssociation
        }
    }

    private suspend fun initializeListPersonnel(listeAssociations: List<AssociationPersonnelRapportChantier>): List<Personnel>? {
        return withContext(Dispatchers.IO) {

            val listePersonnel = mutableListOf<Personnel>()

            listeAssociations.forEach { association ->
                if (listePersonnel.find { it.personnelId == association.personnelId } == null) {
                    val personnel =
                        dataSourcePersonnel.getPersonnelById(association.personnelId.toLong())
                    listePersonnel.add(personnel)
                }
            }
            listePersonnel
        }
    }

    ///// LOAD DATA MATERIEL /////

    private suspend fun loadDataMateriel(listRapportChantiersIds: List<Long>) {
        uiScope.launch {

            listeAssociationsMaterielRapportChantier.value =
                loadListAssociationMaterielRapportChantier(listRapportChantiersIds)

            _listeMaterielRapportChantier.value =
                initializeListMateriel(listeAssociationsMaterielRapportChantier.value!!)
        }.join()
    }

    private suspend fun loadListAssociationMaterielRapportChantier(listRapportChantiersIds: List<Long>): List<AssociationMaterielRapportChantier> {
        return withContext(Dispatchers.IO) {
            var listeAssociation =
                dataSourceAssociationMaterielRapportChantierDao.getAssociationsMaterielRapportChantierByRapportChantierIdsLists(
                    listRapportChantiersIds
                )
            listeAssociation
        }
    }

    private suspend fun initializeListMateriel(
        listeAssociation: List<AssociationMaterielRapportChantier>
    ): List<Materiel>? {
        return withContext(Dispatchers.IO) {
            val listeMateriel = mutableListOf<Materiel>()

            listeAssociation.forEach { association ->
                if (listeMateriel.find { it.id == association.materielId } == null) {
                    val materiel =
                        dataSourceMateriel.getMaterielById(association.materielId.toLong())
                    listeMateriel.add(materiel)
                }
            }
            listeMateriel
        }

    }

    ///// LOAD DATA MATERIEL LOCATION /////

    private suspend fun loadDataMaterielLocation(listRapportChantiersIds: List<Long>) {
        uiScope.launch {

            listeAssociationsMaterielLocationRapportChantier.value =
                loadListAssociationMaterielLocationRapportChantier(listRapportChantiersIds)

            _listeMaterielLocationRapportChantier.value =
                initializeListMaterielLocation(listeAssociationsMaterielLocationRapportChantier.value!!)
        }.join()
    }

    private suspend fun loadListAssociationMaterielLocationRapportChantier(listRapportChantiersIds: List<Long>): List<AssociationMaterielLocationRapportChantier> {
        return withContext(Dispatchers.IO) {
            val listeAssociation =
                dataSourceAssociationMaterielLocationRapportChantierDao.getAssociationsMaterielLocationRapportChantierByRapportChantierIdsLists(
                    listRapportChantiersIds
                )
            listeAssociation
        }
    }

    private suspend fun initializeListMaterielLocation(
        listeAssociation: List<AssociationMaterielLocationRapportChantier>
    ): List<MaterielLocation>? {
        return withContext(Dispatchers.IO) {
            val listeMaterielLocation = mutableListOf<MaterielLocation>()

            listeAssociation.forEach { association ->
                if (listeMaterielLocation.find { it.id == association.materielLocationId } == null) {
                    val materielLocation =
                        dataSourceMaterielLocation.getMaterielLocationById(association.materielLocationId.toLong())
                    listeMaterielLocation.add(materielLocation)
                }
            }
            listeMaterielLocation
        }

    }

    ///// LOAD DATA MATERIAUX /////

    private suspend fun loadDataMateriaux(listRapportChantiersIds: List<Long>) {

        uiScope.launch {

            listeAssociationsMateriauxRapportChantier.value =
                loadListAssociationMateriauxRapportChantier(listRapportChantiersIds)

            _listeMateriauxRapportChantier.value =
                initializeListMateriaux(listeAssociationsMateriauxRapportChantier.value!!)

        }.join()
    }

    private suspend fun loadListAssociationMateriauxRapportChantier(listRapportChantiersIds: List<Long>): List<AssociationMateriauxRapportChantier> {
        return withContext(Dispatchers.IO) {
            var listAssociation =
                dataSourceAssociationMateriauxRapportChantierDao.getAssociationsMateriauxRapportChantierByRapportChantierIdsList(
                    listRapportChantiersIds
                )
            listAssociation
        }
    }

    private suspend fun initializeListMateriaux(listeAssociation: List<AssociationMateriauxRapportChantier>): List<Materiaux>? {
        return withContext(Dispatchers.IO) {
            val listeMateriaux = mutableListOf<Materiaux>()

            listeAssociation.forEach { association ->
                if (listeMateriaux.find { it.id == association.materiauxId } == null) {
                    val materiaux =
                        dataSourceMateriaux.getMateriauxById(association.materiauxId.toLong())
                    listeMateriaux.add(materiaux)
                }
            }
            listeMateriaux

        }
    }

    ///// LOAD DATA SOUS TRAITANCE /////

    private suspend fun loadDataSousTraitance(listRapportChantiersIds: List<Long>) {
        uiScope.launch {

            listeAssociationsSousTraitanceRapportChantier.value =
                loadListAssociationSousTraitanceRapportChantier(listRapportChantiersIds)

            _listeSousTraitanceRapportChantier.value =
                initializeListSousTraitance(listeAssociationsSousTraitanceRapportChantier.value!!)
        }.join()
    }

    private suspend fun loadListAssociationSousTraitanceRapportChantier(listRapportChantiersIds: List<Long>): List<AssociationSousTraitanceRapportChantier> {
        return withContext(Dispatchers.IO) {
            val listeAssociation =
                dataSourceAssociationSousTraitanceRapportChantierDao.getAssociationsSousTraitanceRapportChantierByRapportChantierIdsList(
                    listRapportChantiersIds
                )
            listeAssociation
        }
    }

    private suspend fun initializeListSousTraitance(
        listeAssociation: List<AssociationSousTraitanceRapportChantier>
    ): List<SousTraitance>? {
        return withContext(Dispatchers.IO) {
            val listeSousTraitance = mutableListOf<SousTraitance>()

            listeAssociation.forEach { association ->
                if (listeSousTraitance.find { it.id == association.sousTraitanceId } == null) {
                    val sousTraitance =
                        dataSourceSousTraitance.getSousTraitanceById(association.sousTraitanceId.toLong())
                    listeSousTraitance.add(sousTraitance)
                }
            }
            listeSousTraitance
        }

    }


    fun onClickChantier(chantier: Chantier) {
        selectedChantier.value = chantier
        _navigation.value = GestionNavigation.SELECTION_DATE

    }

    fun onDatesSelected(date1: Long, date2: Long) {

        loadDataFromDates(date1, date2)
        _navigation.value = GestionNavigation.DONNEES_SELECTIONNEES
    }

    fun onBoutonClicked() {
        _navigation.value = GestionNavigation.EN_ATTENTE
    }


    ///// TABLEAU PERSONNEL /////

    fun creationListeNombreHeuresTravaillees() {

        val liste: MutableList<Tableau> = mutableListOf()
        val listeNames: MutableList<Tableau> = mutableListOf(Tableau(""))
        val listeTotal: MutableList<Tableau> = mutableListOf(Tableau("TOTAL"))

        //Ajout des dates
        _listRapportsChantier.value?.forEach {
            liste.add(Tableau(it.dateRapportChantier.toString()))
        }
//        Version 2
        _listePersonnelRapportChantier.value?.forEach { personnel ->
            listeNames.add(Tableau(personnel.nom!!))
            var totalPersonnel = 0

            _listRapportsChantier.value?.forEach { rapportChantier ->


                listeAssociationsPersonnelRapportChantier.value?.find { it.personnelId == personnel.personnelId && it.rapportChantierID == rapportChantier.rapportChantierId }?.NbHeuresTravaillees?.let {
                    totalPersonnel += it
                }
                val string =
                    listeAssociationsPersonnelRapportChantier.value?.find { it.personnelId == personnel.personnelId && it.rapportChantierID == rapportChantier.rapportChantierId }?.NbHeuresTravaillees.toString()

                if (string != "null") {
                    Timber.i("value string = $string")
                    liste.add(Tableau(string))
                } else {
                    liste.add(Tableau("0"))
                }
            }
            listeTotal.add(Tableau(totalPersonnel.toString()))
        }

        _tableauPersonnel.value = liste
        _listeNames.value = listeNames
        _listeTotalNbHeures.value = listeTotal
    }


    fun onClickButtonVisibilityTotalHeuresTravailles() {
        showTabHeuresTravaillees.value = !showTabHeuresTravaillees.value!!
        Timber.i("showTabHeuresTravaillees = ${showTabHeuresTravaillees.value}")
    }

    ///// TABLEAU MATERIEL /////

    fun creationTableauMateriel() {
        val liste: MutableList<Tableau> = mutableListOf()
        val listeNames: MutableList<Tableau> = mutableListOf(Tableau(""))
        val listeTotal: MutableList<Tableau> = mutableListOf(Tableau("TOTAL"))

        //Ajout des dates
        _listRapportsChantier.value?.forEach {
            liste.add(Tableau(it.dateRapportChantier.toString()))
        }

        _listeMaterielRapportChantier.value?.forEach { materiel ->
            listeNames.add(Tableau(materiel.modele!!))
            var totalMateriel = 0

            _listRapportsChantier.value?.forEach { rapportChantier ->

                listeAssociationsMaterielRapportChantier.value?.find { it.materielId == materiel.id && it.rapportChantierID == rapportChantier.rapportChantierId }?.NbHeuresUtilisees?.let {
                    totalMateriel += it
                }

                val string =
                    listeAssociationsMaterielRapportChantier.value?.find { it.materielId == materiel.id && it.rapportChantierID == rapportChantier.rapportChantierId }?.NbHeuresUtilisees.toString()

                if (string != "null") {
                    Timber.i("value string = $string")
                    liste.add(Tableau(string))
                } else {
                    liste.add(Tableau("0"))
                }
            }
            listeTotal.add(Tableau(totalMateriel.toString()))
        }

        _tableauMateriel.value = liste
        _listeNomsMateriel.value = listeNames
        _listeTotalNbHeuresMateriel.value = listeTotal

    }

    fun onClickButtonVisibilityTotalMateriel() {
        showTabMateriel.value = !showTabMateriel.value!!
        Timber.i("showTabHeuresTravaillees = ${showTabMateriel.value}")
    }

    ///// TABLEAU MATERIAUX /////

    fun creationTableauMateriaux() {
        val liste: MutableList<Tableau> = mutableListOf()
        val listeNames: MutableList<Tableau> = mutableListOf(Tableau(""))
        val listeTotal: MutableList<Tableau> = mutableListOf(Tableau("TOTAL"))

        //Ajout des dates
        _listRapportsChantier.value?.forEach {
            liste.add(Tableau(it.dateRapportChantier.toString()))
        }

        _listeMateriauxRapportChantier.value?.forEach { materiaux ->
            listeNames.add(Tableau(materiaux.description))
            var totalMateriaux = 0

            _listRapportsChantier.value?.forEach { rapportChantier ->

                listeAssociationsMateriauxRapportChantier.value?.find { it.materiauxId == materiaux.id && it.rapportChantierID == rapportChantier.rapportChantierId }?.quantite?.let {
                    totalMateriaux += it
                }

                val string =
                    listeAssociationsMateriauxRapportChantier.value?.find { it.materiauxId == materiaux.id && it.rapportChantierID == rapportChantier.rapportChantierId }?.quantite
                        .toString()

                if (string != "null") {
                    Timber.i("value string = $string")
                    liste.add(Tableau(string))
                } else {
                    liste.add(Tableau("0"))
                }
            }
            listeTotal.add(Tableau(totalMateriaux.toString()))
        }

        _tableauMateriaux.value = liste
        _listeNomsMateriaux.value = listeNames
        _listeTotalNbHeuresMateriaux.value = listeTotal

    }

    fun onClickButtonVisibilityTotalMateriaux() {
        showTabMateriaux.value = !showTabMateriaux.value!!
    }


    fun onClickGenerateXlsxFile(): Uri {

        Timber.i("onClickGenerateXlsxFile")

        val context = getApplication<Application>().applicationContext
        val inputStream = context.resources.openRawResource(R.raw.classeur1)

        val xlwb = WorkbookFactory.create(inputStream)

        //Row index specifies the row in the worksheet (starting at 0):
        val rowNumber = 0
        //Cell index specifies the column within the chosen row (starting at 0):
        val columnNumber = 0

        var firstSheet = true
        var firstDateOfSheet = ""

        //Boucle creation Sheets avec dates et données en-tête
        totalDates.forEachIndexed { index, date ->
            val sdf = DateTimeFormatter.ofPattern("dd-MM")
            val weekFiled: WeekFields = WeekFields.of(Locale.FRANCE)
            if (firstSheet) {
                firstDateOfSheet = sdf.format(date)
                Timber.i("firstDateOfSheet = $firstDateOfSheet")
                xlwb.setSheetName(
                    xlwb.numberOfSheets - 1,
                    "SEMAINE ${date.get(weekFiled.weekOfWeekBasedYear())}"
                )
                firstSheet = false
            }

            if (index > 0 && index < totalDates.size - 1) {
                if (totalDates[index - 1].dayOfWeek.value >= date.dayOfWeek.value) {
                    val lastDateOfSheet = sdf.format(
                        totalDates[index - 1].with(
                            WeekFields.of(Locale.FRANCE).dayOfWeek(), 5L
                        )
                    )
                    val xlWs = xlwb.getSheetAt(xlwb.numberOfSheets - 1)
                    Timber.i("firstDateOfSheet 2 = $firstDateOfSheet")
                    xlWs.getRow(0).getCell(0)
                        .setCellValue("RAPPORT HEBDOMADAIRE du $firstDateOfSheet au $lastDateOfSheet")
                    xlWs.getRow(1).getCell(3).setCellValue("du $firstDateOfSheet")
                    xlWs.getRow(1).getCell(6).setCellValue("au $lastDateOfSheet")
                    xlWs.getRow(1).getCell(13)
                        .setCellValue("Chantier n° ${selectedChantier.value!!.numeroChantier}")
                    xlWs.getRow(1).getCell(15)
                        .setCellValue(selectedChantier.value!!.adresseChantier.adresseToString())
                    firstDateOfSheet = sdf.format(date)
                    Timber.i("firstDateOfSheet 3 = $firstDateOfSheet")
                    xlwb.cloneSheet(0)
                    // GENERATION TITRE SHEET
                    xlwb.setSheetName(
                        xlwb.numberOfSheets - 1,
                        "SEMAINE ${date.get(weekFiled.weekOfWeekBasedYear())}"
                    )
                    Timber.i("MAKING NEW SHEET")
                }
            } else if (index == totalDates.size - 1) {
                Timber.i("Dernier index")
                val lastDateOfSheet = sdf.format(
                    totalDates[index - 1].with(
                        WeekFields.of(Locale.FRANCE).dayOfWeek(),
                        5L
                    )
                )
                val xlWs = xlwb.getSheetAt(xlwb.numberOfSheets - 1)
                Timber.i("firstDateOfSheet 2 = $firstDateOfSheet")
                xlWs.getRow(0).getCell(0)
                    .setCellValue("RAPPORT HEBDOMADAIRE du $firstDateOfSheet au $lastDateOfSheet")
                xlWs.getRow(1).getCell(3).setCellValue("du $firstDateOfSheet")
                xlWs.getRow(1).getCell(6).setCellValue("au $lastDateOfSheet")
            }
        }

        // Remplissage Heures PERSONNEL

        val listePersonnel: MutableList<Personnel> = mutableListOf()
        val listePersonnelInterimaire: MutableList<Personnel> = mutableListOf()

        Timber.i("Passage boucle alpha")
        // Generation liste Personnel et Personnel Interimaire
        _listePersonnelRapportChantier.value?.forEach { personnel ->
            Timber.i("Passage boucle personnel 0 ${personnel.nom}")
            if (!personnel.interimaire) {
                listePersonnel.add(personnel)
            } else listePersonnelInterimaire.add(personnel)
        }


        var rowToEdit = 3
        listePersonnel.forEach { personnel ->
            var sheetToEdit = 0
            var xlWs = xlwb.getSheetAt(sheetToEdit)
            xlWs.getRow(rowToEdit).getCell(0)
                .setCellValue(personnel.prenom + " " + personnel.nom)
            _listRapportsChantier.value?.forEachIndexed { index, rapportChantier ->

                if (index > 0) {
                    if (_listRapportsChantier.value!![index - 1].dateRapportChantier!!.dayOfWeek.value >= rapportChantier.dateRapportChantier!!.dayOfWeek.value) {
                        sheetToEdit += 1
                        xlWs = xlwb.getSheetAt(sheetToEdit)
                        xlWs.getRow(rowToEdit).getCell(0)
                            .setCellValue(personnel.prenom + " " + personnel.nom)
                    }
                }
                //xlWs = xlwb.getSheetAt(sheetToEdit)
                val value =
                    listeAssociationsPersonnelRapportChantier.value?.find { it.personnelId == personnel.personnelId && it.rapportChantierID == rapportChantier.rapportChantierId }
                        ?.NbHeuresTravaillees?.toDouble()

                when (rapportChantier.dateRapportChantier?.dayOfWeek) {
                    DayOfWeek.MONDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(5).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.TUESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(6).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.WEDNESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(7).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.THURSDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(8).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.FRIDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(9).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.SATURDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(10).setCellValue(
                            value
                        )
                    }

                    else -> Timber.i("ERROR")
                }
            }
            rowToEdit++
        }

        // PERSONNEL INTERIMAIRE
        rowToEdit = 14
        listePersonnelInterimaire.forEach { personnel ->
            var sheetToEdit = 0
            var xlWs = xlwb.getSheetAt(sheetToEdit)
            xlWs.getRow(rowToEdit).getCell(0)
                .setCellValue(personnel.prenom + " " + personnel.nom)
            _listRapportsChantier.value?.forEachIndexed { index, rapportChantier ->
                if (index > 0) {
                    if (_listRapportsChantier.value!![index - 1].dateRapportChantier!!.dayOfWeek.value >= rapportChantier.dateRapportChantier!!.dayOfWeek.value) {
                        sheetToEdit++
                        Timber.i("sheetToEdit = $sheetToEdit")
                        xlWs = xlwb.getSheetAt(sheetToEdit)
                        xlWs.getRow(rowToEdit).getCell(0)
                            .setCellValue(personnel.prenom + " " + personnel.nom)
                    }
                }
                xlWs = xlwb.getSheetAt(sheetToEdit)
                val value =
                    listeAssociationsPersonnelRapportChantier.value?.find { it.personnelId == personnel.personnelId && it.rapportChantierID == rapportChantier.rapportChantierId }
                        ?.NbHeuresTravaillees?.toDouble()

                when (rapportChantier.dateRapportChantier?.dayOfWeek) {
                    DayOfWeek.MONDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(5).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.TUESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(6).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.WEDNESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(7).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.THURSDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(8).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.FRIDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(9).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.SATURDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(10).setCellValue(
                            value
                        )
                    }

                    else -> Timber.i("ERROR")
                }
            }
            rowToEdit++
        }

        // MATERIEL
        rowToEdit = 20
        listeMaterielRapportChantier.value?.forEach { materiel ->
            var sheetToEdit = 0
            var xlWs = xlwb.getSheetAt(sheetToEdit)
            xlWs.getRow(rowToEdit).getCell(0)
                .setCellValue(materiel.marque + " " + materiel.modele)
            _listRapportsChantier.value?.forEachIndexed { index, rapportChantier ->
                if (index > 0) {
                    if (_listRapportsChantier.value!![index - 1].dateRapportChantier!!.dayOfWeek.value >= rapportChantier.dateRapportChantier!!.dayOfWeek.value) {
                        sheetToEdit++
                        Timber.i("sheetToEdit = $sheetToEdit")
                        xlWs = xlwb.getSheetAt(sheetToEdit)
                        xlWs.getRow(rowToEdit).getCell(0)
                            .setCellValue(materiel.marque + " " + materiel.modele)
                    }
                }
                xlWs = xlwb.getSheetAt(sheetToEdit)
                val value =
                    listeAssociationsMaterielRapportChantier.value?.find { it.materielId == materiel.id && it.rapportChantierID == rapportChantier.rapportChantierId }
                        ?.NbHeuresUtilisees?.toDouble()

                when (rapportChantier.dateRapportChantier?.dayOfWeek) {
                    DayOfWeek.MONDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(5).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.TUESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(6).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.WEDNESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(7).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.THURSDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(8).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.FRIDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(9).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.SATURDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(10).setCellValue(
                            value
                        )
                    }
                    else -> Timber.i("ERROR")
                }
            }
            rowToEdit++
        }

        // MATERIEL LOCATION
        rowToEdit = 35
        listeMaterielLocationRapportChantier.value?.forEach { materielLocation ->
            var sheetToEdit = 0
            var xlWs = xlwb.getSheetAt(sheetToEdit)
            xlWs.getRow(rowToEdit).getCell(0)
                .setCellValue(materielLocation.fournisseur)
            xlWs.getRow(rowToEdit).getCell(1)
                .setCellValue(materielLocation.designation)

            _listRapportsChantier.value?.forEachIndexed { index, rapportChantier ->
                if (index > 0) {
                    if (_listRapportsChantier.value!![index - 1].dateRapportChantier!!.dayOfWeek.value >= rapportChantier.dateRapportChantier!!.dayOfWeek.value) {
                        sheetToEdit++
                        Timber.i("sheetToEdit = $sheetToEdit")
                        xlWs = xlwb.getSheetAt(sheetToEdit)
                        xlWs.getRow(rowToEdit).getCell(0)
                            .setCellValue(materielLocation.fournisseur)
                        xlWs.getRow(rowToEdit).getCell(1)
                            .setCellValue(materielLocation.designation)
                    }
                }
                xlWs = xlwb.getSheetAt(sheetToEdit)
                val value =
                    listeAssociationsMaterielLocationRapportChantier.value?.find { it.materielLocationId == materielLocation.id && it.rapportChantierID == rapportChantier.rapportChantierId }
                        ?.NbHeuresUtilisees?.toDouble()

                when (rapportChantier.dateRapportChantier?.dayOfWeek) {
                    DayOfWeek.MONDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(5).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.TUESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(6).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.WEDNESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(7).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.THURSDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(8).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.FRIDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(9).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.SATURDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(10).setCellValue(
                            value
                        )
                    }
                    else -> Timber.i("ERROR")
                }
            }
            rowToEdit++
        }

        // MATERIAUX

        rowToEdit = 43
        listeMateriauxRapportChantier.value?.forEach { materiaux ->
            var sheetToEdit = 0
            var xlWs = xlwb.getSheetAt(sheetToEdit)
            xlWs.getRow(rowToEdit).getCell(0).setCellValue(materiaux.fournisseur)
            xlWs.getRow(rowToEdit).getCell(1).setCellValue(materiaux.description)
            xlWs.getRow(rowToEdit).getCell(2).setCellValue(materiaux.nDeBon)
            _listRapportsChantier.value?.forEachIndexed { index, rapportChantier ->
                if (index > 0) {
                    if (_listRapportsChantier.value!![index - 1].dateRapportChantier!!.dayOfWeek.value >= rapportChantier.dateRapportChantier!!.dayOfWeek.value) {
                        sheetToEdit++
                        Timber.i("sheetToEdit = $sheetToEdit")
                        xlWs = xlwb.getSheetAt(sheetToEdit)
                        xlWs.getRow(rowToEdit).getCell(0).setCellValue(materiaux.fournisseur)
                        xlWs.getRow(rowToEdit).getCell(1).setCellValue(materiaux.description)
                        xlWs.getRow(rowToEdit).getCell(2).setCellValue(materiaux.nDeBon)
                    }
                }
                xlWs = xlwb.getSheetAt(sheetToEdit)
                val value =
                    listeAssociationsMateriauxRapportChantier.value?.find { it.materiauxId == materiaux.id && it.rapportChantierID == rapportChantier.rapportChantierId }
                        ?.quantite?.toDouble()


                when (rapportChantier.dateRapportChantier?.dayOfWeek) {
                    DayOfWeek.MONDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(5).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.TUESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(6).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.WEDNESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(7).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.THURSDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(8).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.FRIDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(9).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.SATURDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(10).setCellValue(
                            value
                        )
                    }
                    else -> Timber.i("ERROR")
                }
            }
            rowToEdit++
        }

        // MATERIEL LOCATION
        rowToEdit = 52
        listeSousTraitanceRapportChantier.value?.forEach { SousTraitance ->
            var sheetToEdit = 0
            var xlWs = xlwb.getSheetAt(sheetToEdit)
            xlWs.getRow(rowToEdit).getCell(0)
                .setCellValue(SousTraitance.societe)
            xlWs.getRow(rowToEdit).getCell(1)
                .setCellValue(SousTraitance.prestations)


            _listRapportsChantier.value?.forEachIndexed { index, rapportChantier ->
                if (index > 0) {
                    if (_listRapportsChantier.value!![index - 1].dateRapportChantier!!.dayOfWeek.value >= rapportChantier.dateRapportChantier!!.dayOfWeek.value) {
                        sheetToEdit++
                        Timber.i("sheetToEdit = $sheetToEdit")
                        xlWs = xlwb.getSheetAt(sheetToEdit)
                        xlWs.getRow(rowToEdit).getCell(0)
                            .setCellValue(SousTraitance.societe)
                        xlWs.getRow(rowToEdit).getCell(1)
                            .setCellValue(SousTraitance.prestations)
                    }
                }
                xlWs = xlwb.getSheetAt(sheetToEdit)
                val value =
                    listeAssociationsSousTraitanceRapportChantier.value?.find { it.sousTraitanceId == SousTraitance.id && it.rapportChantierID == rapportChantier.rapportChantierId }
                        ?.quantite?.toDouble()

                when (rapportChantier.dateRapportChantier?.dayOfWeek) {
                    DayOfWeek.MONDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(5).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.TUESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(6).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.WEDNESDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(7).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.THURSDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(8).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.FRIDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(9).setCellValue(
                            value
                        )
                    }

                    DayOfWeek.SATURDAY -> value?.let {
                        xlWs.getRow(rowToEdit).getCell(10).setCellValue(
                            value
                        )
                    }
                    else -> Timber.i("ERROR")
                }
            }
            rowToEdit++
        }


        // COMMENTAIRES ET NOM REPONSABLE
        var sheetToEdit = 0
        var xlWs = xlwb.getSheetAt(sheetToEdit)
        val chefChantierName =
            _listePersonnelRapportChantier.value?.find { it.personnelId == selectedChantier.value!!.chefChantierId }?.nom
                ?: "ERREUR"
        xlWs.getRow(56).getCell(6).setCellValue("Etabli par $chefChantierName")


        _listRapportsChantier.value?.forEachIndexed { index, rapportChantier ->
            if (index > 0) {
                if (_listRapportsChantier.value!![index - 1].dateRapportChantier!!.dayOfWeek.value >= rapportChantier.dateRapportChantier!!.dayOfWeek.value) {
                    sheetToEdit++
                    Timber.i("sheetToEdit = $sheetToEdit")
                    xlWs = xlwb.getSheetAt(sheetToEdit)
                    xlWs.getRow(56).getCell(6).setCellValue("Etabli par $chefChantierName")

                }
            }
//            xlWs = xlwb.getSheetAt(sheetToEdit)
            val value = rapportChantier.observations ?: ""

            when (rapportChantier.dateRapportChantier?.dayOfWeek) {
                DayOfWeek.MONDAY -> xlWs.getRow(20).getCell(12).setCellValue(value)

                DayOfWeek.TUESDAY -> xlWs.getRow(26).getCell(12).setCellValue(value)

                DayOfWeek.WEDNESDAY -> xlWs.getRow(32).getCell(12).setCellValue(value)

                DayOfWeek.THURSDAY -> xlWs.getRow(38).getCell(12).setCellValue(value)

                DayOfWeek.FRIDAY -> xlWs.getRow(44).getCell(12).setCellValue(value)

                DayOfWeek.SATURDAY -> xlWs.getRow(50).getCell(12).setCellValue(value)

                else -> Timber.i("ERROR")
            }

        }


        val fileName = "FileName.xlsx"

        val extStorageDirectory: String =
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                .toString()
        val folder = File(
            extStorageDirectory,
            "Rapports de chantier"
        ) // Name of the folder you want to keep your file in the local storage.

        folder.mkdir() //creating the folder

        Timber.i("errorExcel $extStorageDirectory ")

        val file = File(extStorageDirectory, fileName)
        try {
            file.createNewFile() // creating the file inside the folder
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        try {
            val fileOut = FileOutputStream(file) //Opening the file
            xlwb.write(fileOut) //Writing all your row column inside the file
            fileOut.close() //closing the file and done
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val uri =
            FileProvider.getUriForFile(
                context,
                "com.example.gestionnairerapportdechantier" + ".fileprovider",
                file
            )
        return uri
    }
}