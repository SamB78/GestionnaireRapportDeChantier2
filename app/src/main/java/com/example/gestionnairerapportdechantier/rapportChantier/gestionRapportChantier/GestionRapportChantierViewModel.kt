package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.database.*
import com.example.gestionnairerapportdechantier.entities.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class GestionRapportChantierViewModel(
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
    val date: Long = -1L
) : ViewModel() {

    enum class GestionNavigation {
        PASSAGE_GESTION_PERSONNEL,
        VALIDATION_GESTION_PERSONNEL,
        PASSAGE_AJOUT_PERSONNEL,

        PASSAGE_AUTRES_INFORMATIONS,
        PASSAGE_ETAPE_2_AUTRES_INFORMATIONS,
        VALIDATION_AUTRES_INFORMATIONS,

        PASSAGE_OBSERVATIONS,
        VALIDATION_OBSERVATIONS,

        PASSAGE_GESTION_MATERIEL,
        PASSAGE_AJOUT_MATERIEL,
        VALIDATION_GESTION_MATERIEL,

        PASSAGE_GESTION_MATERIEL_LOCATION,
        PASSAGE_AJOUT_MATERIEL_LOCATION,
        VALIDATION_AJOUT_MATERIEL_LOCATION,
        VALIDATION_GESTION_MATERIEL_LOCATION,

        PASSAGE_GESTION_MATERIAUX,
        PASSAGE_AJOUT_MATERIAUX,
        VALIDATION_AJOUT_MATERIAUX,
        VALIDATION_GESTION_MATERIAUX,

        VALIDATION_GESTION_SOUS_TRAITANCE,
        PASSAGE_AJOUT_SOUS_TRAITANCE,
        VALIDATION_AJOUT_SOUS_TRAITANCE,
        PASSAGE_GESTION_SOUS_TRAITANCE,

        ENREGISTREMENT_CHANTIER,

        ANNULATION,
        EN_ATTENTE,
    }

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // Rapport chantier
    private var _rapportChantier = MutableLiveData<RapportChantier>()
    val rapportChantier: LiveData<RapportChantier>
        get() = this._rapportChantier

    private var infosRapportChantier = MutableLiveData<InfosRapportChantier>()
    var meteo = MutableLiveData<Meteo>()
    var observations = MutableLiveData<String>()


    private var _chantier = MutableLiveData<Chantier>()
    val chantier: LiveData<Chantier>
        get() = this._chantier

    //Liste personnel lié au chantier
    private var _listePersonnelRapportChantier = MutableLiveData<MutableList<Personnel>>(
        mutableListOf()
    )
    val listePersonnelRapportChantier: LiveData<MutableList<Personnel>>
        get() = this._listePersonnelRapportChantier

    // Liste materiel lié au rapport de chantier
    private var _listeMaterielRapportChantier =
        MutableLiveData<MutableList<Materiel>>(mutableListOf())
    val listeMaterielRapportChantier: LiveData<MutableList<Materiel>>
        get() = this._listeMaterielRapportChantier

    // Gestion Materiel Location
    var newMaterielLocation = MutableLiveData<MaterielLocation>()

    private var _listeMaterielLocationRapportChantier =
        MutableLiveData<MutableList<MaterielLocation>>(mutableListOf())
    val listeMaterielLocationRapportChantier: LiveData<MutableList<MaterielLocation>>
        get() = this._listeMaterielLocationRapportChantier

    // Gestion Materiaux
    var newMateriaux = MutableLiveData<Materiaux>()

    private var _listeMateriauxRapportChantier = MutableLiveData<MutableList<Materiaux>>(
        mutableListOf()
    )
    val listeMateriauxRapportChantier: LiveData<MutableList<Materiaux>>
        get() = this._listeMateriauxRapportChantier


    // Gestion sous traitance
    var newSousTraitance = MutableLiveData<SousTraitance>()

    private var _listeSousTraitanceRapportChantier = MutableLiveData<MutableList<SousTraitance>>(
        mutableListOf()
    )
    val listeSousTraitanceRapportChantier: LiveData<MutableList<SousTraitance>>
        get() = this._listeSousTraitanceRapportChantier


    // Associations avec le Rapport de chantier
    private var listeAssociationsPersonnelRapportChantier =
        MutableLiveData<MutableList<AssociationPersonnelRapportChantier>>()

    private var listeAssociationsMaterielRapportChantier =
        MutableLiveData<MutableList<AssociationMaterielRapportChantier>>()

    private var listeAssociationsMaterielLocationRapportChantier =
        MutableLiveData<MutableList<AssociationMaterielLocationRapportChantier>>()

    private var listeAssociationsMateriauxRapportChantier =
        MutableLiveData<MutableList<AssociationMateriauxRapportChantier>>()

    private var listeAssociationsSousTraitanceRapportChantier =
        MutableLiveData<MutableList<AssociationSousTraitanceRapportChantier>>()

    //Booleans Actualisation données
    private var isDataPersonnelNeededToActualize = false
    private var isDataMaterielNeededToActualize = false


    // Comptage nombre de personnes travaillant

    val nbPersonnesTravaillant = MutableLiveData<Int>(0)
    val quantiteMaterielUtilise = MutableLiveData<Int>(0)
    val quantiteMaterielLocationUtilise = MutableLiveData<Int>(0)
    val quantiteMateriauxUtilise = MutableLiveData<Int>(0)
    val quantiteSousTraitanceUtilise = MutableLiveData<Int>(0)
    var quantiteInformationsConformes = MutableLiveData<Int>(0)
    var quantiteInformationsNonConformes = MutableLiveData<Int>(0)


    //Verification sur les données ont été changées sans avoir été sauvegardées
    val dataChangedWithoutSave = MutableLiveData<DataSaved>(DataSaved())

    //Boolean onClick
    private var _observationClicked = MutableLiveData<Boolean>(false)
    val observationClicked: LiveData<Boolean>
        get() = _observationClicked

    //Navigation
    private var _navigation = MutableLiveData<GestionNavigation>()
    val navigation: LiveData<GestionNavigation>
        get() = _navigation

    init {
        initializeData(rapportChantierId, date)
        infosRapportChantier.value = InfosRapportChantier()
        meteo.value = Meteo()
        onBoutonClicked()
    }

    private fun initializeData(rapportChantierId: Long, date: Long) {
        when {
            rapportChantierId != -1L -> {
                // Si id Rapport chantier retourné (Rapport chantier existe déjà)
                uiScope.launch {
                    _rapportChantier.value = getRapportChantierValue(rapportChantierId)
                    _chantier.value = getChantier(_rapportChantier.value?.chantierId)
                    loadData()
                }
            }
            date != -1L -> {
                val date2 = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
                uiScope.launch {
                    _chantier.value = getChantier(chantierId)
                    _rapportChantier.value = getRapportChantierValueByDate(date2)
                    if (_rapportChantier.value?.chantierId == null) {
                        // Si nouveau rapport de Chantier
                        _rapportChantier.value = RapportChantier(
                            null,
                            chantierId,
                            _chantier.value!!.chefChantierId,
                            date2
                        )
                        _rapportChantier.value!!.rapportChantierId = sendNewRapportChantierToDB()
                        initializeDataPersonnelForNewRapportChantier(_rapportChantier.value!!.rapportChantierId!!.toLong())
                    }
                    _chantier.value = getChantier(chantierId)
                    loadData()
                }
            }
            else -> {
                Timber.e("ERROR: PAS DE DATE OU D'ID")
            }
        }
    }

    /////////////////////// LOAD DONNEES ////////////////////////////////////////

    private fun loadData() {
        loadDataPersonnel(_rapportChantier.value!!.rapportChantierId!!.toLong())
        loadDataMaterielFromRapportChantier(_rapportChantier.value!!.rapportChantierId!!.toLong())
        loadDataMaterielLocationFromRapportChantier(_rapportChantier.value!!.rapportChantierId!!.toLong())
        loadDataMateriauxFromRapportChantier(_rapportChantier.value!!.rapportChantierId!!.toLong())
        loadDataSousTraitanceFromRapportChantier(_rapportChantier.value!!.rapportChantierId!!.toLong())
        loadDataInfosRapportChantier()
    }


    /////////////////////// INITIALISATION DONNEES ////////////////////////////////////////

    private suspend fun getChantier(chantierId: Int?): Chantier? {
        return withContext(Dispatchers.IO) {
            val chantier = chantierId?.toLong()?.let { dataSourceChantier.getChantierById(it) }
            chantier
        }
    }

    private suspend fun getRapportChantierValue(id: Long): RapportChantier {
        return withContext(Dispatchers.IO) {
            val rapportChantier = dataSourceRapportChantier.getRapportChantierById(id)
            rapportChantier
        }
    }

    private suspend fun getRapportChantierValueByDate(date: LocalDate): RapportChantier? {
        return withContext(Dispatchers.IO) {
            val rapportChantier = dataSourceRapportChantier.getRapportChantierByDate(date)
            rapportChantier
        }
    }

    private suspend fun initializeDataPersonnel(chantierId: Int): List<Personnel>? {
        return withContext(Dispatchers.IO) {
            Timber.i("Entrée initializeDataPersonnel")
            val listeAssociation =
                dataSourceAssociationPersonnelChantier.getAssociationPersonnelChantierIdByChantierId(
                    chantierId
                )
            val listePersonnel =
                listeAssociation.let { dataSourcePersonnel.getPersonnelsByIds(it) }
            listePersonnel
        }
    }

    /// INITIALISATION DES DONNEES POUR NOUVEAU RAPPORT DE CHANTIER DEPUIS DONNEES CHANTIER

    private suspend fun initializeDataPersonnelForNewRapportChantier(rapportChantierId: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val listeAssociation =
                    dataSourceAssociationPersonnelChantier.getAssociationPersonnelChantierIdByChantierId(
                        chantierId
                    )
                listeAssociation.forEach {
                    dataSourceAssociationPersonnelRapportChantierDao.insertAssociationPersonnelRapportChantier(
                        AssociationPersonnelRapportChantier(null, it, rapportChantierId.toInt())
                    )
                }
            }
        }.join()
    }


    ///// PERSONNEL //////
    private fun loadDataPersonnel(rapportChantierId: Long) {

        uiScope.launch {
            listeAssociationsPersonnelRapportChantier.value =
                loadListAssociationPersonnelRapportChantier(rapportChantierId) as MutableList<AssociationPersonnelRapportChantier>

            _listePersonnelRapportChantier.value =
                initializeListPersonnel(listeAssociationsPersonnelRapportChantier.value!!) as MutableList<Personnel>
        }
    }

    private fun updateDataPersonnelAfterPersonnelAdded(rapportChantierId: Long) {
        uiScope.launch {
            val nouvelleListeAssociation =
                loadListAssociationPersonnelRapportChantier(rapportChantierId)
            val listeDifferencesAssociation =
                nouvelleListeAssociation.minus(listeAssociationsPersonnelRapportChantier.value!!)
            listeAssociationsPersonnelRapportChantier.value!!.addAll(listeDifferencesAssociation)

            val listeNouveauPersonnel = initializeListPersonnel(listeDifferencesAssociation)
            _listePersonnelRapportChantier.value = listeNouveauPersonnel?.let {
                _listePersonnelRapportChantier.value?.plus(it) as MutableList<Personnel>
            }
        }
    }

    private suspend fun initializeListPersonnel(listeAssociation: List<AssociationPersonnelRapportChantier>): List<Personnel> {
        return withContext(Dispatchers.IO) {

            nbPersonnesTravaillant.postValue(0)
            val listePersonnel = mutableListOf<Personnel>()

            listeAssociation.forEach {
                val personnel = dataSourcePersonnel.getPersonnelById(it.personnelId.toLong())
                personnel.nombreHeuresTravaillees = it.NbHeuresTravaillees
                listePersonnel.add(personnel)
                if (it.NbHeuresTravaillees > 0) {
                    nbPersonnesTravaillant.postValue(nbPersonnesTravaillant.value!! + 1)
                }
            }
            listePersonnel
        }
    }

    private suspend fun loadListAssociationPersonnelRapportChantier(rapportChantierId: Long): List<AssociationPersonnelRapportChantier> {
        return withContext(Dispatchers.IO) {
            val listAssociation =
                dataSourceAssociationPersonnelRapportChantierDao.getAssociationsPersonnelRapportChantierByRapportChantierId(
                    rapportChantierId
                )
            listAssociation
        }
    }


    //// MATERIEL ////

    private fun loadDataMaterielFromRapportChantier(rapportChantierId: Long) {
        uiScope.launch {

            listeAssociationsMaterielRapportChantier.value =
                loadListAssociationMaterielRapportChantier(rapportChantierId) as MutableList<AssociationMaterielRapportChantier>

            _listeMaterielRapportChantier.value =
                initializeListMateriel(listeAssociationsMaterielRapportChantier.value!!) as MutableList<Materiel>
        }
    }

    private suspend fun loadListAssociationMaterielRapportChantier(rapportChantierId: Long): List<AssociationMaterielRapportChantier> {
        return withContext(Dispatchers.IO) {
            val listeAssociation =
                dataSourceAssociationMaterielRapportChantierDao.getAssociationsMaterielRapportChantierByRapportChantierId(
                    rapportChantierId
                )
            listeAssociation
        }
    }

    private fun updateDataMaterielAfterMaterielAdded(rapportChantierId: Long) {
        uiScope.launch {
            val nouvelleListeAssociation =
                loadListAssociationMaterielRapportChantier(rapportChantierId)
            val listeDifferencesAssociation =
                nouvelleListeAssociation.minus(listeAssociationsMaterielRapportChantier.value!!)
            listeAssociationsMaterielRapportChantier.value!!.addAll(listeDifferencesAssociation)

            val listeNouveauxMateriel = initializeListMateriel(listeDifferencesAssociation)
            _listeMaterielRapportChantier.value = listeNouveauxMateriel?.let {
                _listeMaterielRapportChantier.value?.plus(it) as MutableList<Materiel>
            }
        }
    }

    private suspend fun initializeListMateriel(
        listeAssociation: List<AssociationMaterielRapportChantier>
    ): List<Materiel> {
        return withContext(Dispatchers.IO) {
            quantiteMaterielUtilise.postValue(0)
            val listeMateriel = mutableListOf<Materiel>()

            listeAssociation.forEach {
                val materiel = dataSourceMateriel.getMaterielById(it.materielId.toLong())
                materiel.nombreHeuresUtilisees = it.NbHeuresUtilisees
                listeMateriel.add(materiel)
                if (it.NbHeuresUtilisees > 0) {
                    quantiteMaterielUtilise.postValue(quantiteMaterielUtilise.value!! + 1)
                }
            }
            listeMateriel
        }

    }

    //// MATERIEL LOCATION ////

    fun loadDataMaterielLocationFromRapportChantier(rapportChantierId: Long) {
        uiScope.launch {

            listeAssociationsMaterielLocationRapportChantier.value =
                loadListAssociationMaterielLocationRapportChantier(rapportChantierId) as MutableList<AssociationMaterielLocationRapportChantier>

            _listeMaterielLocationRapportChantier.value =
                initializeListMaterielLocation(listeAssociationsMaterielLocationRapportChantier.value!!) as MutableList<MaterielLocation>
        }
    }

    private suspend fun loadListAssociationMaterielLocationRapportChantier(rapportChantierId: Long): List<AssociationMaterielLocationRapportChantier> {
        return withContext(Dispatchers.IO) {
            val listeAssociation =
                dataSourceAssociationMaterielLocationRapportChantierDao.getAssociationsMaterielLocationRapportChantierByRapportChantierId(
                    rapportChantierId
                )
            listeAssociation
        }
    }

    private fun updateDataMaterielLocationAfterMaterielLocationAdded(rapportChantierId: Long) {
        uiScope.launch {
            val nouvelleListeAssociation =
                loadListAssociationMaterielLocationRapportChantier(rapportChantierId)
            val listeDifferencesAssociation =
                nouvelleListeAssociation.minus(listeAssociationsMaterielLocationRapportChantier.value!!)
            listeAssociationsMaterielLocationRapportChantier.value!!.addAll(
                listeDifferencesAssociation
            )

            val listeNouveauxMaterielLocation =
                initializeListMaterielLocation(listeDifferencesAssociation)
            _listeMaterielLocationRapportChantier.value = listeNouveauxMaterielLocation?.let {
                _listeMaterielLocationRapportChantier.value?.plus(it) as MutableList<MaterielLocation>
            }
        }
    }

    private suspend fun initializeListMaterielLocation(
        listeAssociation: List<AssociationMaterielLocationRapportChantier>
    ): List<MaterielLocation>? {
        return withContext(Dispatchers.IO) {
            quantiteMaterielLocationUtilise.postValue(0)
            val listeMaterielLocation = mutableListOf<MaterielLocation>()

            listeAssociation.forEach {
                val materielLocation =
                    dataSourceMaterielLocation.getMaterielLocationById(it.materielLocationId.toLong())
                materielLocation.quantite = it.NbHeuresUtilisees
                listeMaterielLocation.add(materielLocation)
                if (it.NbHeuresUtilisees > 0) {
                    quantiteMaterielLocationUtilise.postValue(quantiteMaterielLocationUtilise.value!! + 1)
                }
            }
            listeMaterielLocation
        }

    }


    //// MATERIAUX ////


    fun loadDataMateriauxFromRapportChantier(rapportChantierId: Long) {

        uiScope.launch {

            listeAssociationsMateriauxRapportChantier.value =
                loadListAssociationMateriauxRapportChantier(rapportChantierId) as MutableList<AssociationMateriauxRapportChantier>

            _listeMateriauxRapportChantier.value =
                initializeListMateriaux(listeAssociationsMateriauxRapportChantier.value!!) as MutableList<Materiaux>

        }
    }

    private fun updateDataMateriauxAfterMateriauxAdded(rapportChantierId: Long) {
        uiScope.launch {
            val nouvelleListeAssociation =
                loadListAssociationMateriauxRapportChantier(rapportChantierId)
            val listeDifferencesAssociation =
                nouvelleListeAssociation.minus(listeAssociationsMateriauxRapportChantier.value!!)
            listeAssociationsMateriauxRapportChantier.value!!.addAll(listeDifferencesAssociation)

            val listeNouveauxMateriaux = initializeListMateriaux(listeDifferencesAssociation)
            _listeMateriauxRapportChantier.value = listeNouveauxMateriaux?.let {
                _listeMateriauxRapportChantier.value?.plus(it) as MutableList<Materiaux>
            }
        }
    }

    private suspend fun loadListAssociationMateriauxRapportChantier(rapportChantierId: Long): List<AssociationMateriauxRapportChantier> {
        return withContext(Dispatchers.IO) {
            val listAssociation =
                dataSourceAssociationMateriauxRapportChantierDao.getAssociationsMateriauxRapportChantierByRapportChantierId(
                    rapportChantierId
                )
            listAssociation
        }
    }

    private suspend fun initializeListMateriaux(listeAssociation: List<AssociationMateriauxRapportChantier>): List<Materiaux>? {
        return withContext(Dispatchers.IO) {
            quantiteMateriauxUtilise.postValue(0)
            val listeMateriaux = mutableListOf<Materiaux>()

            listeAssociation.forEach {
                val materiaux = dataSourceMateriaux.getMateriauxById(it.materiauxId.toLong())
                materiaux.quantite = it.quantite
                listeMateriaux.add(materiaux)
                if (it.quantite > 0) {
                    quantiteMateriauxUtilise.postValue(quantiteMateriauxUtilise.value!! + 1)
                }
            }
            listeMateriaux

        }
    }

    private fun loadDataInfosRapportChantier() {
        infosRapportChantier.value = _rapportChantier.value?.infosRapportChantier
        quantiteInformationsConformes.value = infosRapportChantier.value!!.sendNumberOfTrueChamps()
        quantiteInformationsNonConformes.value =
            infosRapportChantier.value!!.sendNumberOfFalseChamps()
    }


    ///// SOUS-TRAITANTS /////

    fun loadDataSousTraitanceFromRapportChantier(rapportChantierId: Long) {

        uiScope.launch {

            listeAssociationsSousTraitanceRapportChantier.value =
                loadListAssociationSousTraitanceRapportChantier(rapportChantierId) as MutableList<AssociationSousTraitanceRapportChantier>

            _listeSousTraitanceRapportChantier.value =
                initializeListSousTraitance(listeAssociationsSousTraitanceRapportChantier.value!!) as MutableList<SousTraitance>

        }
    }

    private fun updateDataSousTraitanceAfterSousTraitanceAdded(rapportChantierId: Long) {
        uiScope.launch {
            val nouvelleListeAssociation =
                loadListAssociationSousTraitanceRapportChantier(rapportChantierId)
            val listeDifferencesAssociation =
                nouvelleListeAssociation.minus(listeAssociationsSousTraitanceRapportChantier.value!!)
            listeAssociationsSousTraitanceRapportChantier.value!!.addAll(listeDifferencesAssociation)

            val listeNouveauxSousTraitance =
                initializeListSousTraitance(listeDifferencesAssociation)
            _listeSousTraitanceRapportChantier.value = listeNouveauxSousTraitance?.let {
                _listeSousTraitanceRapportChantier.value?.plus(it) as MutableList<SousTraitance>
            }
        }
    }

    private suspend fun loadListAssociationSousTraitanceRapportChantier(rapportChantierId: Long): List<AssociationSousTraitanceRapportChantier> {
        return withContext(Dispatchers.IO) {
            var listAssociation =
                dataSourceAssociationSousTraitanceRapportChantierDao.getAssociationsSousTraitanceRapportChantierByRapportChantierId(
                    rapportChantierId
                )
            listAssociation
        }
    }

    private suspend fun initializeListSousTraitance(listeAssociation: List<AssociationSousTraitanceRapportChantier>): List<SousTraitance>? {
        return withContext(Dispatchers.IO) {
            quantiteSousTraitanceUtilise.postValue(0)
            val listeSousTraitance = mutableListOf<SousTraitance>()

            listeAssociation.forEach {
                val sousTraitance =
                    dataSourceSousTraitance.getSousTraitanceById(it.sousTraitanceId.toLong())
                sousTraitance.quantite = it.quantite
                listeSousTraitance.add(sousTraitance)
                if (it.quantite > 0) {
                    quantiteSousTraitanceUtilise.postValue(quantiteSousTraitanceUtilise.value!! + 1)
                }
            }
            listeSousTraitance

        }
    }


/////////////////////// GESTION RAPPORT CHANTIER ////////////////////////////////////////

    fun onClickCheckBoxChantier() {

        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceRapportChantier.updateChantierBooleanFromId(
                    rapportChantier.value!!.rapportChantierId!!.toLong(),
                    rapportChantier.value!!.chantier
                )
                _rapportChantier.value!!.chantier =
                    dataSourceRapportChantier.getChantierBooleanFromId(rapportChantier.value!!.rapportChantierId!!.toLong())
            }
            _rapportChantier.value = _rapportChantier.value
        }
    }

    fun onClickCheckBoxEntretien() {

        Timber.i("onClickCheckBoxEntretien() value chantierBoolean: before launche uiScope ${_rapportChantier.value!!.entretien} id: ${rapportChantier.value!!.rapportChantierId!!.toLong()} ")
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceRapportChantier.updateEntretienBooleanFromId(
                    rapportChantier.value!!.rapportChantierId!!.toLong(),
                    rapportChantier.value!!.entretien
                )
                _rapportChantier.value!!.entretien =
                    dataSourceRapportChantier.getEntretienBooleanFromId(rapportChantier.value!!.rapportChantierId!!.toLong())
                Timber.i("onClickCheckBoxEntretien() 2 value chantierBoolean: ${_rapportChantier.value!!.entretien}")
            }
            _rapportChantier.value = _rapportChantier.value
            Timber.i("onClickCheckBoxEntretien() 3 value chantierBoolean: ${_rapportChantier.value!!.entretien}")
        }
    }

/////////////////////// GESTION PERSONNEL ////////////////////////////////////////

    fun onClickButtonGestionPersonnel() {
        _navigation.value = GestionNavigation.PASSAGE_GESTION_PERSONNEL
    }

    fun onClickButtonAddPersonnel() {
        isDataPersonnelNeededToActualize = true
        _navigation.value = GestionNavigation.PASSAGE_AJOUT_PERSONNEL
    }


    fun onClickButtonValidationGestionPersonnel() {

        var nombreTotalHeuresTravailles: Int = 0
        var nombreTotalHeuresInterimTravailles: Int = 0
        _listePersonnelRapportChantier.value?.forEach { listePersonnelChantierValide ->

            listeAssociationsPersonnelRapportChantier.value?.find { it.personnelId == listePersonnelChantierValide.personnelId }?.NbHeuresTravaillees =
                listePersonnelChantierValide.nombreHeuresTravaillees
            when (listePersonnelChantierValide.interimaire) {
                true -> {
                    nombreTotalHeuresInterimTravailles += listePersonnelChantierValide.nombreHeuresTravaillees
                }
                false -> {
                    nombreTotalHeuresTravailles += listePersonnelChantierValide.nombreHeuresTravaillees
                }
            }
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceAssociationPersonnelRapportChantierDao.updateListAssociationPersonnelRapportChantier(
                    listeAssociationsPersonnelRapportChantier.value!!
                )
            }

            _rapportChantier.value!!.totalMOInterimaire = nombreTotalHeuresInterimTravailles
            _rapportChantier.value!!.totalMOPersonnel = nombreTotalHeuresTravailles
            _rapportChantier.value!!.totalMO =
                _rapportChantier.value!!.totalMOInterimaire + _rapportChantier.value!!.totalMOPersonnel
            _rapportChantier.value!!.dataSaved.dataPersonnel = true
            updateRapportChantierInDB()
            loadDataPersonnel(_rapportChantier.value!!.rapportChantierId!!.toLong())
            dataChangedWithoutSave.value!!.dataPersonnel = false
            _navigation.value = GestionNavigation.VALIDATION_GESTION_PERSONNEL
        }
    }

    fun onClickDeletePersonnel(item: Personnel) {
        _listePersonnelRapportChantier.value!!.remove(item)
        listeAssociationsPersonnelRapportChantier.value!!.filter { it.personnelId == item.personnelId }
            .forEach {
                listeAssociationsPersonnelRapportChantier.value!!.remove(it)
            }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceAssociationPersonnelRapportChantierDao.deleteAssociationPersonnelRapportChantierByPersonnelId(
                    item.personnelId!!.toLong(),
                    _rapportChantier.value!!.rapportChantierId!!.toLong()
                )
            }
        }
        _listePersonnelRapportChantier.value = _listePersonnelRapportChantier.value
    }


    fun onResumeGestionPersonnelFragment() {
        if (isDataPersonnelNeededToActualize) {
            Timber.i("onResumePersonnel")
            updateDataPersonnelAfterPersonnelAdded(_rapportChantier.value!!.rapportChantierId!!.toLong())
            isDataPersonnelNeededToActualize = false
        }
    }


    fun onPersonnelProgressChanged(progress: Int, personnel: Personnel) {
        _listePersonnelRapportChantier.value!!.find { it.personnelId == personnel.personnelId }?.nombreHeuresTravaillees =
            progress

        dataChangedWithoutSave.value!!.dataPersonnel = true
    }

    /////////////////////// GESTION MATERIEL ////////////////////////////////////////

    fun onClickButtonGestionMateriel() {
        _navigation.value = GestionNavigation.PASSAGE_GESTION_MATERIEL
    }

    fun onClickButtonAddMateriel() {
        isDataMaterielNeededToActualize = true
        _navigation.value = GestionNavigation.PASSAGE_AJOUT_MATERIEL
    }


    fun onMaterielProgressChanged(progress: Int, materiel: Materiel) {

        _listeMaterielRapportChantier.value!!.find { it.id == materiel.id }?.nombreHeuresUtilisees =
            progress
        dataChangedWithoutSave.value!!.dataMateriel = true
    }


    fun onClickButtonValidationGestionMateriel() {
        var nombreTotalHeuresUtilisees: Int = 0
        var nombreTotalHeuresInterimTravailles: Int = 0
        _listeMaterielRapportChantier.value?.forEach { listeMaterielRapportChantier ->
            listeAssociationsMaterielRapportChantier.value?.find { it.materielId == listeMaterielRapportChantier.id }?.NbHeuresUtilisees =
                listeMaterielRapportChantier.nombreHeuresUtilisees
            nombreTotalHeuresUtilisees += listeMaterielRapportChantier.nombreHeuresUtilisees
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceAssociationMaterielRapportChantierDao.updateListAssociationMaterielRapportChantier(
                    listeAssociationsMaterielRapportChantier.value!!
                )
            }
            _rapportChantier.value!!.totalHeuresMaterielSociete = nombreTotalHeuresUtilisees

            _rapportChantier.value!!.dataSaved.dataMateriel = true
            updateRapportChantierInDB()
            loadDataMaterielFromRapportChantier(_rapportChantier.value!!.rapportChantierId!!.toLong())
            dataChangedWithoutSave.value!!.dataMateriel = false
            _navigation.value = GestionNavigation.VALIDATION_GESTION_MATERIEL
        }
    }

    fun onClickDeleteMateriel(item: Materiel) {
        _listeMaterielRapportChantier.value!!.remove(item)
        listeAssociationsMaterielRapportChantier.value!!.filter { it.materielId == item.id }
            .forEach {
                listeAssociationsMaterielRapportChantier.value!!.remove(it)
            }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceAssociationMaterielRapportChantierDao.deleteAssociationRapportChantierByMaterielId(
                    item.id!!.toLong(),
                    _rapportChantier.value!!.rapportChantierId!!.toLong()
                )
            }
        }
        _listeMaterielRapportChantier.value = _listeMaterielRapportChantier.value
    }

    fun onResumeGestionMaterielFragment() {
        if (isDataMaterielNeededToActualize) {
            Timber.i("onResume")
            updateDataMaterielAfterMaterielAdded(_rapportChantier.value!!.rapportChantierId!!.toLong())
            isDataMaterielNeededToActualize = false
        }
    }
    /////////////////////// GESTION MATERIEL LOCATION /////////////////////////////////////

    fun onClickButtonGestionMaterielLocation() {
        _navigation.value = GestionNavigation.PASSAGE_GESTION_MATERIEL_LOCATION
    }

    fun onClickButtonAddMaterielLocation() {
        newMaterielLocation.value = MaterielLocation()
        _navigation.value = GestionNavigation.PASSAGE_AJOUT_MATERIEL_LOCATION
    }

    fun onClickButtonConfirmationAddMaterielLocation() {
        uiScope.launch {
            val idMaterielLocation = saveNewMaterielLocationInDB(newMaterielLocation.value!!)
            generateNewAssociationMaterielLocationRapportChantier(idMaterielLocation)
            onResumeGestionMaterielLocationFragment()
            _navigation.value = GestionNavigation.VALIDATION_AJOUT_MATERIEL_LOCATION
            newMateriaux.value = Materiaux()
        }
    }

    private suspend fun saveNewMaterielLocationInDB(value: MaterielLocation): Long {
        return withContext(Dispatchers.IO) {
            dataSourceMaterielLocation.insertMaterielLocation(value)
        }
    }

    private suspend fun generateNewAssociationMaterielLocationRapportChantier(idMateriaux: Long) {
        withContext(Dispatchers.IO) {
            dataSourceAssociationMaterielLocationRapportChantierDao.insertAssociationMaterielLocationRapportChantier(
                AssociationMaterielLocationRapportChantier(
                    null,
                    idMateriaux.toInt(),
                    _rapportChantier.value!!.rapportChantierId!!,
                    newMaterielLocation.value!!.quantite
                )
            )
        }

    }

    fun onClickButtonAnnulationAddMaterielLocation() {
        newMaterielLocation.value = MaterielLocation()
    }

    fun onMaterielLocationProgressChanged(progress: Int) {
        newMaterielLocation.value!!.quantite = progress

    }

    fun onMaterielLocationProgressChanged(progress: Int, materiel: MaterielLocation) {
        _listeMaterielLocationRapportChantier.value!!.find { it.id == materiel.id }?.quantite =
            progress
        dataChangedWithoutSave.value!!.dataMaterielLocation = true
    }


    fun onClickButtonValidationGestionMaterielLocation() {
        var nombreTotalHeuresUtilisees = 0
        _listeMaterielLocationRapportChantier.value?.forEach { listeMaterielLocationRapportChantier ->
            listeAssociationsMaterielLocationRapportChantier.value?.find { it.materielLocationId == listeMaterielLocationRapportChantier.id }?.NbHeuresUtilisees =
                listeMaterielLocationRapportChantier.quantite
            nombreTotalHeuresUtilisees += listeMaterielLocationRapportChantier.quantite
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceAssociationMaterielLocationRapportChantierDao.updateListAssociationMaterielLocationRapportChantier(
                    listeAssociationsMaterielLocationRapportChantier.value!!
                )
            }
            _rapportChantier.value!!.totalHeuresMaterielLocation = nombreTotalHeuresUtilisees

            _rapportChantier.value!!.dataSaved.dataMaterielLocation = true
            updateRapportChantierInDB()
            loadDataMaterielLocationFromRapportChantier(_rapportChantier.value!!.rapportChantierId!!.toLong())
            dataChangedWithoutSave.value!!.dataMaterielLocation = false
            _navigation.value = GestionNavigation.VALIDATION_GESTION_MATERIEL_LOCATION
        }
    }

    fun onClickDeleteMaterielLocation(item: MaterielLocation) {
        _listeMaterielLocationRapportChantier.value!!.remove(item)
        listeAssociationsMaterielLocationRapportChantier.value!!.filter { it.materielLocationId == item.id }
            .forEach {
                listeAssociationsMaterielLocationRapportChantier.value!!.remove(it)
            }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceAssociationMaterielLocationRapportChantierDao.deleteAssociationRapportChantierByMaterielLocationId(
                    item.id!!.toLong(),
                    _rapportChantier.value!!.rapportChantierId!!.toLong()
                )
            }
        }
        _listeMaterielLocationRapportChantier.value = _listeMaterielLocationRapportChantier.value
    }

    private fun onResumeGestionMaterielLocationFragment() {
        updateDataMaterielLocationAfterMaterielLocationAdded(_rapportChantier.value!!.rapportChantierId!!.toLong())

    }

    /////////////////////// GESTION DES MATERIAUX ////////////////////////////////////////

    fun onClickButtonGestionMateriaux() {
        _navigation.value = GestionNavigation.PASSAGE_GESTION_MATERIAUX
    }

    fun onClickButtonAddMateriaux() {
        newMateriaux.value = Materiaux()
        _navigation.value = GestionNavigation.PASSAGE_AJOUT_MATERIAUX
    }

    fun onClickButtonConfirmationAddMateriaux() {
        uiScope.launch {
            val idMateriaux = saveNewMateriauxInDB(newMateriaux.value!!)
            generateNewAssociationMateriauxRapportChantier(idMateriaux)
            onResumeGestionMateriauxFragment()
            _navigation.value = GestionNavigation.VALIDATION_AJOUT_MATERIAUX
            newMateriaux.value = Materiaux()
        }
    }

    private suspend fun saveNewMateriauxInDB(value: Materiaux): Long {
        return withContext(Dispatchers.IO) {
            dataSourceMateriaux.insertMateriaux(value)
        }
    }

    private suspend fun generateNewAssociationMateriauxRapportChantier(idMateriaux: Long) {
        withContext(Dispatchers.IO) {
            dataSourceAssociationMateriauxRapportChantierDao.insertAssociationMateriauxRapportChantier(
                AssociationMateriauxRapportChantier(
                    null,
                    idMateriaux.toInt(),
                    _rapportChantier.value!!.rapportChantierId!!,
                    newMateriaux.value!!.quantite
                )
            )
        }

    }

    fun onClickButtonAnnulationAddMateriaux() {
        newMateriaux.value = Materiaux()
    }


    fun onMateriauxProgressChanged(progress: Int) {
        newMateriaux.value!!.quantite = progress
    }

    fun onMateriauxProgressChanged(progress: Int, materiaux: Materiaux = Materiaux()) {
        _listeMateriauxRapportChantier.value!!.find { it.id == materiaux.id }?.quantite =
            progress
        dataChangedWithoutSave.value!!.dataMateriaux = true
    }

    fun onClickButtonValidationGestionMateriaux() {
        var quantiteTotalMateriaux = 0
        _listeMateriauxRapportChantier.value?.forEach { listeMateriauxRapportChantier ->
            listeAssociationsMateriauxRapportChantier.value?.find { it.materiauxId == listeMateriauxRapportChantier.id }?.quantite =
                listeMateriauxRapportChantier.quantite
            quantiteTotalMateriaux += listeMateriauxRapportChantier.quantite
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceAssociationMateriauxRapportChantierDao.updateListAssociationMateriauxRapportChantier(
                    listeAssociationsMateriauxRapportChantier.value!!
                )
            }
            _rapportChantier.value!!.totalMateriaux = quantiteTotalMateriaux

            _rapportChantier.value!!.dataSaved.dataMateriaux = true
            updateRapportChantierInDB()
            loadDataMateriauxFromRapportChantier(_rapportChantier.value!!.rapportChantierId!!.toLong())
            dataChangedWithoutSave.value!!.dataMateriaux = false
            _navigation.value = GestionNavigation.VALIDATION_GESTION_MATERIAUX
        }
    }

    fun onClickDeleteMateriaux(item: Materiaux) {
        _listeMateriauxRapportChantier.value!!.remove(item)
        listeAssociationsMateriauxRapportChantier.value!!.filter { it.materiauxId == item.id }
            .forEach {
                listeAssociationsMateriauxRapportChantier.value!!.remove(it)
            }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceAssociationMateriauxRapportChantierDao.deleteAssociationMateriauxRapportChantierByMateriauxId(
                    item.id.toLong(),
                    _rapportChantier.value!!.rapportChantierId!!.toLong()
                )
            }
        }
        _listeMateriauxRapportChantier.value = _listeMateriauxRapportChantier.value
    }


    private fun onResumeGestionMateriauxFragment() {
        Timber.i("onResume")
        updateDataMateriauxAfterMateriauxAdded(_rapportChantier.value!!.rapportChantierId!!.toLong())
    }


    /////////////////////// GESTION SOUS-TRAITANCE /////////////////////////////////////

    fun onClickButtonGestionSousTraitance() {
        _navigation.value = GestionNavigation.PASSAGE_GESTION_SOUS_TRAITANCE
    }

    fun onClickButtonAddSousTraitance() {
        newSousTraitance.value = SousTraitance()
        _navigation.value = GestionNavigation.PASSAGE_AJOUT_SOUS_TRAITANCE
    }

    fun onClickButtonConfirmationAddSousTraitance() {
        uiScope.launch {
            val idSousTraitance = saveNewSousTratianceInDB(newSousTraitance.value!!)
            generateNewAssociationSousTraitanceRapportChantier(idSousTraitance)
            onResumeGestionSousTraitanceFragment()
            _navigation.value = GestionNavigation.VALIDATION_AJOUT_SOUS_TRAITANCE
            newMateriaux.value = Materiaux()
        }
    }

    private suspend fun saveNewSousTratianceInDB(value: SousTraitance): Long {
        return withContext(Dispatchers.IO) {
            dataSourceSousTraitance.insertSousTraitance(value)
        }
    }

    private suspend fun generateNewAssociationSousTraitanceRapportChantier(idSousTraitance: Long) {
        withContext(Dispatchers.IO) {
            dataSourceAssociationSousTraitanceRapportChantierDao.insertAssociationSousTraitanceRapportChantier(
                AssociationSousTraitanceRapportChantier(
                    null,
                    idSousTraitance.toInt(),
                    _rapportChantier.value!!.rapportChantierId!!,
                    newSousTraitance.value!!.quantite
                )
            )
        }

    }

    fun onClickButtonAnnulationAddSousTraitance() {
        newSousTraitance.value = SousTraitance()
    }

    fun onSousTratianceProgressChanged(progress: Int) {
        newSousTraitance.value!!.quantite = progress
    }

    fun onSousTraitanceProgressChanged(progress: Int, materiel: SousTraitance) {

        _listeSousTraitanceRapportChantier.value!!.find { it.id == materiel.id }?.quantite =
            progress
        dataChangedWithoutSave.value!!.dataSousTraitance = true
    }


    fun onClickButtonValidationGestionSousTraitance() {
        var quantiteTotaleSousTraitance: Int = 0
        _listeSousTraitanceRapportChantier.value?.forEach { listeSousTraitanceRapportChantier ->
            listeAssociationsSousTraitanceRapportChantier.value?.find { it.sousTraitanceId == listeSousTraitanceRapportChantier.id }?.quantite =
                listeSousTraitanceRapportChantier.quantite
            quantiteTotaleSousTraitance += listeSousTraitanceRapportChantier.quantite
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceAssociationSousTraitanceRapportChantierDao.updateListAssociationSousTraitanceRapportChantier(
                    listeAssociationsSousTraitanceRapportChantier.value!!
                )
            }
            _rapportChantier.value!!.totalSousTraitance = quantiteTotaleSousTraitance

            _rapportChantier.value!!.dataSaved.dataSousTraitance = true
            updateRapportChantierInDB()
            loadDataSousTraitanceFromRapportChantier(_rapportChantier.value!!.rapportChantierId!!.toLong())
            dataChangedWithoutSave.value!!.dataSousTraitance = false
            _navigation.value = GestionNavigation.VALIDATION_GESTION_SOUS_TRAITANCE
        }
    }

    fun onClickDeleteSousTraitance(item: SousTraitance) {
        _listeSousTraitanceRapportChantier.value!!.remove(item)
        listeAssociationsSousTraitanceRapportChantier.value!!.filter { it.sousTraitanceId == item.id }
            .forEach {
                listeAssociationsSousTraitanceRapportChantier.value!!.remove(it)
            }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceAssociationSousTraitanceRapportChantierDao.deleteAssociationSousTraitanceRapportChantierBySousTraitanceId(
                    item.id.toLong(),
                    _rapportChantier.value!!.rapportChantierId!!.toLong()
                )
            }
        }
        _listeSousTraitanceRapportChantier.value = _listeSousTraitanceRapportChantier.value
    }

    fun onResumeGestionSousTraitanceFragment() {
        Timber.i("onResume")
        updateDataSousTraitanceAfterSousTraitanceAdded(_rapportChantier.value!!.rapportChantierId!!.toLong())
    }


    /////////////////////// SAUVEGARDE DE DONNEES ////////////////////////////////////////

    fun onClickButtonCreationOrModificationEnded() {
        Timber.i("Chantier ready to save in DB = ${_rapportChantier.value?.chantierId}")
        updateRapportChantierInDB()

        _navigation.value = GestionNavigation.ENREGISTREMENT_CHANTIER

        //OPTIMISATION POSSIBLE AVEC LES PRIVATE SUSPEND FUN
    }

    private fun updateRapportChantierInDB() {

        _rapportChantier.value!!.totalHeuresMateriel =
            _rapportChantier.value!!.totalHeuresMaterielSociete + _rapportChantier.value!!.totalHeuresMaterielLocation

        _rapportChantier.value!!.totalRapportChantier =
            _rapportChantier.value!!.totalHeuresMateriel + _rapportChantier.value!!.totalMO


        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceRapportChantier.update(rapportChantier.value!!)
            }
        }
    }

    private suspend fun sendNewRapportChantierToDB(): Int {
        return withContext(Dispatchers.IO) {
            val value = dataSourceRapportChantier.insert(_rapportChantier.value!!)
            value.toInt()
        }
    }

    /////////////////////// SECURITE & ENVIRONNEMENT ////////////////////////////////////////

    fun onClickButtonAutresInformations() {

        _navigation.value = GestionNavigation.PASSAGE_AUTRES_INFORMATIONS
    }

    fun radioGroupsSecurtie(id: Int) {
        when (id) {
            R.id.radioButtonEPI1 -> infosRapportChantier.value?.securiteRespectPortEPI = true
            R.id.radioButtonEPI2 -> infosRapportChantier.value?.securiteRespectPortEPI = false
            R.id.radioButtonBalisage1 -> infosRapportChantier.value?.securiteBalisage = true
            R.id.radioButtonBalisage2 -> infosRapportChantier.value?.securiteBalisage = false
        }
        dataChangedWithoutSave.value!!.dataConformiteChantier = true
    }

    fun radioGroupsEnvironnement(id: Int) {
        when (id) {
            R.id.radioButtonProprete1 -> infosRapportChantier.value?.environnementProprete = true
            R.id.radioButtonProprete2 -> infosRapportChantier.value?.environnementProprete = false
            R.id.radioButtonNonPollution1 -> infosRapportChantier.value?.environnementNonPollution =
                true
            R.id.radioButtonNonPollution2 -> infosRapportChantier.value?.environnementNonPollution =
                false
        }
        dataChangedWithoutSave.value!!.dataConformiteChantier = true
    }

    fun radioGroupsMateriel(id: Int) {
        when (id) {
            R.id.radioButtonPropreteVehicule1 -> infosRapportChantier.value?.propreteVehicule =
                true
            R.id.radioButtonPropreteVehicule2 -> infosRapportChantier.value?.propreteVehicule =
                false

            R.id.radioButtonEntretienMateriel1 -> infosRapportChantier.value?.propreteVehicule =
                true
            R.id.radioButtonEntretienMateriel2 -> infosRapportChantier.value?.propreteVehicule =
                false

            R.id.radioButtonRenduCarnet1 -> infosRapportChantier.value?.renduCarnetDeBord = true
            R.id.radioButtonRenduCarnet2 -> infosRapportChantier.value?.renduCarnetDeBord = false

            R.id.radioButtonRenduBonDecharge1 -> infosRapportChantier.value?.renduBonDecharge =
                true
            R.id.radioButtonRenduBonDecharge2 -> infosRapportChantier.value?.renduBonDecharge =
                false

            R.id.radioButtonRenduBonCarburant1 -> infosRapportChantier.value?.renduBonCarburant =
                true
            R.id.radioButtonRenduBonCarburant2 -> infosRapportChantier.value?.renduBonCarburant =
                false

            R.id.radioButtonRenduFeuillesInterimaire1 -> infosRapportChantier.value?.feuillesInterimaires =
                true
            R.id.radioButtonRenduFeuillesInterimaire2 -> infosRapportChantier.value?.feuillesInterimaires =
                false

            R.id.radioButtonRenduBonDeCommande1 -> infosRapportChantier.value?.bonDeCommande = true
            R.id.radioButtonRenduBonDeCommande2 -> infosRapportChantier.value?.bonDeCommande =
                false

        }
        dataChangedWithoutSave.value!!.dataConformiteChantier = true
    }

    fun onClickPassageEtape2AutresInformations() {
        _navigation.value = GestionNavigation.PASSAGE_ETAPE_2_AUTRES_INFORMATIONS
    }

    fun onClickButtonValidationAutresInformations() {
        _rapportChantier.value!!.infosRapportChantier = infosRapportChantier.value!!
        quantiteInformationsConformes.value = infosRapportChantier.value!!.sendNumberOfTrueChamps()
        quantiteInformationsNonConformes.value =
            infosRapportChantier.value!!.sendNumberOfFalseChamps()
        uiScope.launch {
            _rapportChantier.value!!.dataSaved.dataConformiteChantier = true
            updateRapportChantierInDB()
            loadDataInfosRapportChantier()
            dataChangedWithoutSave.value!!.dataConformiteChantier = false
            _navigation.value = GestionNavigation.VALIDATION_AUTRES_INFORMATIONS
        }
    }

    /////////////////////// OBSERVATIONS ////////////////////////////////////////

    fun onClickButtonObservations() {

        meteo.value = _rapportChantier.value?.meteo
        observations.value = _rapportChantier.value?.observations
        _navigation.value = GestionNavigation.PASSAGE_OBSERVATIONS
    }

    fun onClickEditDataObservations() {
        dataChangedWithoutSave.value!!.dataObservations = true
    }

    fun onClickButtonValidationObservations() {

        uiScope.launch {

            _rapportChantier.value?.meteo = meteo.value!!
            _rapportChantier.value?.observations = observations.value
            _rapportChantier.value!!.dataSaved.dataObservations = true
            updateRapportChantierInDB()
            dataChangedWithoutSave.value!!.dataObservations = false
            _navigation.value = GestionNavigation.VALIDATION_OBSERVATIONS

        }
    }

    fun onClickEditTextObservations() {
        Handler().postDelayed({
            _observationClicked.postValue(true)
        }, 500L)
    }


    fun onClickButtonAnnuler() {
        _navigation.value = GestionNavigation.ANNULATION
    }


    fun onBoutonClicked() {
        _navigation.value = GestionNavigation.EN_ATTENTE
    }

    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }


    companion object {
        const val MAX_HEURES_TRAVAILLEES: Int = 8
        const val MIN_HEURES_TRAVAILLEES: Int = 0
    }


//    fun onClickPlusHorairesTravailles(personnelId: Int): Boolean {
//
//        val nombreHeuresTravaillees =
//            _listePersonnelRapportChantier.value!!.find { it.personnelId == personnelId }!!.nombreHeuresTravaillees
//
//        if (nombreHeuresTravaillees < MAX_HEURES_TRAVAILLEES) {
//            _listePersonnelRapportChantier.value!!.find { it.personnelId == personnelId }?.nombreHeuresTravaillees =
//                nombreHeuresTravaillees.plus(
//                    1
//                )
//
//            _listePersonnelRapportChantier.value = _listePersonnelRapportChantier.value
//            Timber.i("Valeur nbHeuresTravaillees = ${_listePersonnelRapportChantier.value!!.find { it.personnelId == personnelId }} ")
//
//            return true
//        } else {
//            return false
//        }
//    }
//
//    fun onClickMoinsHorairesTravailles(personnelId: Int): Boolean {
//
//        val nombreHeuresTravaillees =
//            _listePersonnelRapportChantier.value!!.find { it.personnelId == personnelId }!!.nombreHeuresTravaillees
//
//        if (nombreHeuresTravaillees > MIN_HEURES_TRAVAILLEES) {
//            _listePersonnelRapportChantier.value!!.find { it.personnelId == personnelId }?.nombreHeuresTravaillees =
//                nombreHeuresTravaillees.minus(
//                    1
//                )
//            _listePersonnelRapportChantier.value = _listePersonnelRapportChantier.value
//            Timber.i("Valeur nbHeuresTravaillees = ${_listePersonnelRapportChantier.value!!.find { it.personnelId == personnelId }} ")
//            return true
//        } else {
//            return false
//        }
//    }
}
