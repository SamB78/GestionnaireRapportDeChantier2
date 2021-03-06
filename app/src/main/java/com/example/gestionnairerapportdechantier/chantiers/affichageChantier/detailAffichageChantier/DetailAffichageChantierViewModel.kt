package com.example.gestionnairerapportdechantier.chantiers.affichageChantier.detailAffichageChantier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.database.AssociationPersonnelChantierDao
import com.example.gestionnairerapportdechantier.database.ChantierDao
import com.example.gestionnairerapportdechantier.database.PersonnelDao
import com.example.gestionnairerapportdechantier.entities.Adresse
import com.example.gestionnairerapportdechantier.entities.Chantier
import com.example.gestionnairerapportdechantier.entities.Personnel
import kotlinx.coroutines.*
import timber.log.Timber

class DetailAffichageChantierViewModel(
    private val dataSourceChantier: ChantierDao,
    private val dataSourceAssociationPersonnelChantier: AssociationPersonnelChantierDao,
    private val dataSourcePersonnel: PersonnelDao,
    private val idChantier: Long = -1
) : ViewModel() {

    //Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //Création du chantier avec son adresse
    var chantier = MutableLiveData<Chantier>()
    var adresse = MutableLiveData<Adresse>()

    //Chef de Chantier
    private var _chefChantierSelectionne = MutableLiveData<Personnel>()
    val chefChantierSelectionne: LiveData<Personnel>
        get() = this._chefChantierSelectionne


    //Liste personnel selectionné pour le chantier
    var _listePersonnelChantierValide = MutableLiveData<List<Personnel>>()
    val listePersonnelChantierValide: LiveData<List<Personnel>>
        get() = this._listePersonnelChantierValide



    init {
        initializeData(idChantier)
        Timber.i("Chantier initialisé  = ${chantier.value?.numeroChantier}")
    }

    private fun initializeData(id: Long) {
        if (id != -1L) {
            uiScope.launch {
                chantier.value = getChantierValue(id)
                adresse.value = chantier.value!!.adresseChantier
                _chefChantierSelectionne.value = getChefChantierValue(chantier.value!!.chefChantierId)
                _listePersonnelChantierValide.value = initializeDataPersonnel()
            }
        } else {
            chantier.value = Chantier()
            adresse.value = Adresse()
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
            var listePersonnel = listeAssociation?.let { dataSourcePersonnel.getPersonnelsByIds(it) }
            listePersonnel
        }
    }

}