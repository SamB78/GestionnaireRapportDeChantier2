package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.*
import com.example.gestionnairerapportdechantier.entities.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GestionRapportChantierViewModel(
    private val dataSourceRapportChantier: RapportChantierDao,
    private val dataSourceChantier: ChantierDao,
    private val dataSourcePersonnel: PersonnelDao,
    private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao,
    private val dataSourceAssociationPersonnelRapportChantierDao: AssociationPersonnelRapportChantierDao,
    rapportChantierId: Long = -1L,
    val chantierId: Int = -1,
    val date: String? = null
) : ViewModel() {

    enum class GestionNavigation {
        ANNULATION,
        PASSAGE_GESTION_PERSONNEL,
        VALIDATION_GESTION_PERSONNEL,
        ENREGISTREMENT_CHANTIER,
        EN_ATTENTE
    }

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    var _rapportChantier = MutableLiveData<RapportChantier>()
    val rapportChantier: LiveData<RapportChantier>
        get() = this._rapportChantier

    var _listeAssociationsPersonnelRapportsChantier =
        mutableListOf<AssociationPersonnelRapportChantier>()

    var _chantier = MutableLiveData<Chantier>()
    val chantier: LiveData<Chantier>
        get() = this._chantier

    //Liste personnel selectionné pour le chantier
    var _listePersonnelChantier = mutableListOf<Personnel>()
    var _listePersonnelChantierValide = MutableLiveData<List<Personnel>>()
    val listePersonnelChantierValide: LiveData<List<Personnel>>
        get() = this._listePersonnelChantierValide


    //Navigation

    private var _navigation = MutableLiveData<GestionNavigation>()
    val navigation: LiveData<GestionNavigation>
        get() = _navigation

    init {
        Timber.i("Date = $date")
        Timber.i(" chantierId = $chantierId")
        initializeData(rapportChantierId, date)
        Timber.i("rapportChantier initialisé  = ${_rapportChantier.value?.chantierId}")
        Timber.i("Nom chantier = {${chantier.value?.nomChantier}}")
        onBoutonClicked()
    }

    private fun initializeData(id: Long, date: String?) {
        when {
            id != -1L -> {
                // Si Rapport de Chantier existe déjà
                uiScope.launch {
                    _rapportChantier.value = getRapportChantierValue(id)
                    Timber.i("Rapport chantier = ${_rapportChantier.value?.chantierId}, id transfere = $id")
                    _chantier.value = getChantier(_rapportChantier.value?.chantierId)
                    _rapportChantier.value?.chantierId?.let {
                        _listePersonnelChantierValide.value = initializeDataPersonnel(it)
                    }
                    generateOldAssociationsPersonnelRapportChantier()
                }
            }
            date != null -> {
                // Si nouveau rapport de Chantier
                _rapportChantier.value = RapportChantier(null, chantierId, null, LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
                uiScope.launch {
                    _chantier.value = getChantier(chantierId)
                    _listePersonnelChantierValide.value = initializeDataPersonnel(chantierId)
                    _rapportChantier.value!!.rapportChantierId = sendNewDataToDB2()
                    Timber.i("rapportChantierId = ${_rapportChantier.value!!.rapportChantierId}")
                    generateNewAssociationsPersonnelRapportChantier()

                }
            }
            else -> {
                Timber.e("ERROR: PAS DE DATE OU D'ID")
            }
        }
    }

    private suspend fun initializeDataPersonnel(chantierId: Int): List<Personnel>? {
        return withContext(Dispatchers.IO) {
            Timber.i("Entrée initializeDataPersonnel")
            var listeAssociation =
                dataSourceAssociationPersonnelChantier.getAssociationPersonnelChantierIdByChantierId(
                    chantierId
                )
            Timber.i(" listeAssociation: Affichage")
            listeAssociation?.forEach {
                Timber.i("listeAssociation =  $it")
            }
            var listePersonnel =
                listeAssociation?.let { dataSourcePersonnel.getPersonnelsByIds(it) }
            listePersonnel?.forEach {
                Timber.i("listePersonnel: ${it.nom}")
            }
            listePersonnel
        }
    }

    private suspend fun getChantier(chantierId: Int?): Chantier? {
        return withContext(Dispatchers.IO) {
            var chantier = chantierId?.toLong()?.let { dataSourceChantier.getChantierById(it) }
            chantier
        }
    }

    private suspend fun getRapportChantierValue(id: Long): RapportChantier? {
        return withContext(Dispatchers.IO) {
            var rapportChantier = dataSourceRapportChantier.getChantierById(id)
            rapportChantier
        }
    }

    private fun generateNewAssociationsPersonnelRapportChantier() {
        uiScope.launch {
            _listePersonnelChantierValide.value?.forEach {

                val associationPersonnelRapportChantier = AssociationPersonnelRapportChantier(
                    null,
                    it.personnelId!!,
                    _rapportChantier.value!!.rapportChantierId!!,
                    0
                )

                associationPersonnelRapportChantier.associationId = withContext(Dispatchers.IO) {
                    val value =
                        dataSourceAssociationPersonnelRapportChantierDao.insertAssociationPersonnelRapportChantier(
                            associationPersonnelRapportChantier
                        )
                    value.toInt()
                }
                _listeAssociationsPersonnelRapportsChantier.add(associationPersonnelRapportChantier)
            }
            //A UTILISER POUR L'AFFICHAGE d'un rapport de chantier existant déjà

//            _listeAssociationsPersonnelRapportsChantier.forEach { associationPersonnelRapportChantier ->
//                Timber.i("Liste association Personnel Rapport chantier: $associationPersonnelRapportChantier")
//
//                _listePersonnelChantierValide.value!!.find { it.personnelId == associationPersonnelRapportChantier.personnelId }?.nombreHeuresTravaillees =
//                    associationPersonnelRapportChantier.NbHeuresTravaillees
//
//
//            }
        }
    }

    private fun generateOldAssociationsPersonnelRapportChantier() {

        uiScope.launch {
            _listePersonnelChantierValide.value?.forEach {

//                val associationPersonnelRapportChantier = AssociationPersonnelRapportChantier(
//                    null,
//                    it.personnelId!!,
//                    _rapportChantier.value!!.rapportChantierId!!
//                )

                val associationPersonnelRapportChantier: AssociationPersonnelRapportChantier =
                    withContext(Dispatchers.IO) {
                        val value =
                            dataSourceAssociationPersonnelRapportChantierDao.getAssociationPersonnelRapportChantierByPersonnelId(
                                it.personnelId!!.toLong(),
                                rapportChantier.value!!.rapportChantierId!!.toLong()
                            )
                        value
                    }
                it.nombreHeuresTravaillees = associationPersonnelRapportChantier.NbHeuresTravaillees

                _listeAssociationsPersonnelRapportsChantier.add(associationPersonnelRapportChantier)
            }
//            //A UTILISER POUR L'AFFICHAGE d'un rapport de chantier existant déjà
//
//            _listeAssociationsPersonnelRapportsChantier.forEach { associationPersonnelRapportChantier ->
//                Timber.i("Liste association Personnel Rapport chantier: $associationPersonnelRapportChantier")
//
//                _listePersonnelChantierValide.value!!.find { it.personnelId == associationPersonnelRapportChantier.personnelId }?.nombreHeuresTravaillees =
//                    associationPersonnelRapportChantier.NbHeuresTravaillees
//
//
//            }
        }

    }

    fun onClickButtonValidationGestionPersonnel() {

        _listePersonnelChantierValide.value?.forEach { listePersonnelChantierValide ->

            _listeAssociationsPersonnelRapportsChantier.find { it.personnelId == listePersonnelChantierValide.personnelId }?.NbHeuresTravaillees =
                listePersonnelChantierValide.nombreHeuresTravaillees
        }

        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceAssociationPersonnelRapportChantierDao.updateListAssociationPersonnelRapportChantier(
                    _listeAssociationsPersonnelRapportsChantier
                )
            }


        }

        _navigation.value = GestionNavigation.VALIDATION_GESTION_PERSONNEL
    }


    fun onBoutonClicked() {
        _navigation.value = GestionNavigation.EN_ATTENTE
    }

    fun onClickButtonCreationOrModificationEnded() {
        Timber.i("Chantier ready to save in DB = ${_rapportChantier.value?.chantierId}")
        if (_rapportChantier.value?.chantierId == null) sendNewDataToDB()
        else updateDataInDB()

        _navigation.value = GestionNavigation.ENREGISTREMENT_CHANTIER

        //OPTIMISATION POSSUBLE AVEC LES PRIVATE SUSPEND FUN
    }

    private fun updateDataInDB() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSourceRapportChantier.update(_rapportChantier.value!!)
            }
        }
    }

    private fun sendNewDataToDB(): Int? {
        var value: Long? = null
        uiScope.launch {
            withContext(Dispatchers.IO) {
                value = dataSourceRapportChantier.insert(_rapportChantier.value!!)
            }
        }
        return value?.toInt()
    }

    private suspend fun sendNewDataToDB2(): Int {
        return withContext(Dispatchers.IO) {
            val value = dataSourceRapportChantier.insert(_rapportChantier.value!!)
            value.toInt()
        }
    }


    fun onClickButtonGestionPersonnel() {
        _navigation.value = GestionNavigation.PASSAGE_GESTION_PERSONNEL
    }


    fun onClickButtonAnnuler() {
        _navigation.value = GestionNavigation.ANNULATION
    }

    fun onClickPlusHorairesTravailles(personnelId: Int): Boolean {

        val nombreHeuresTravaillees =
            _listePersonnelChantierValide.value!!.find { it.personnelId == personnelId }!!.nombreHeuresTravaillees

        if (nombreHeuresTravaillees < MAX_HEURES_TRAVAILLEES) {
            _listePersonnelChantierValide.value!!.find { it.personnelId == personnelId }?.nombreHeuresTravaillees =
                nombreHeuresTravaillees.plus(
                    1
                )!!

            _listePersonnelChantierValide.value = _listePersonnelChantierValide.value
            Timber.i("Valeur nbHeuresTravaillees = ${_listePersonnelChantierValide.value!!.find { it.personnelId == personnelId }} ")

            return true
        }
        else {
            return false
        }
    }


    fun onClickMoinsHorairesTravailles(personnelId: Int): Boolean {

        val nombreHeuresTravaillees =
            _listePersonnelChantierValide.value!!.find { it.personnelId == personnelId }!!.nombreHeuresTravaillees

        if (nombreHeuresTravaillees > MIN_HEURES_TRAVAILLEES) {
            _listePersonnelChantierValide.value!!.find { it.personnelId == personnelId }?.nombreHeuresTravaillees =
                nombreHeuresTravaillees.minus(
                    1
                )!!
            _listePersonnelChantierValide.value = _listePersonnelChantierValide.value
            Timber.i("Valeur nbHeuresTravaillees = ${_listePersonnelChantierValide.value!!.find { it.personnelId == personnelId }} ")
            return true
        } else {
            return false
        }
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

}
