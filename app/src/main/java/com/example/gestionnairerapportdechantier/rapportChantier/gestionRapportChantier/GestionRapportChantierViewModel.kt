package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.Database.RapportChantierDao
import com.example.gestionnairerapportdechantier.entities.RapportChantier
import kotlinx.coroutines.*
import timber.log.Timber

class GestionRapportChantierViewModel(private val dataSource: RapportChantierDao, id: Long = -1L): ViewModel()  {

        enum class gestionNavigation {
            ANNULATION,
            ENREGISTREMENT_CHANTIER,
            EN_ATTENTE
        }

        //Coroutines
        private val viewModelJob = Job()
        private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        var rapportChantier = MutableLiveData<RapportChantier>()

        //Navigation

        private var _navigation = MutableLiveData<gestionNavigation>()
        val navigation: LiveData<gestionNavigation>
            get() = _navigation

        init {
            initializeData(id)
            Timber.i("rapportChantier initialisé  = ${rapportChantier.value?.chantierId}")
            onBoutonClicked()
        }

        private fun initializeData(id: Long) {
            if (id != -1L) {
                uiScope.launch {
                    rapportChantier.value = getRapportChantierValue(id)
                }
            } else {
                rapportChantier.value = RapportChantier()
            }
        }

        private suspend fun getRapportChantierValue(id: Long): RapportChantier? {
            return withContext(Dispatchers.IO) {
                var rapportChantier = dataSource.getChantierById(id)
                rapportChantier
            }
        }

        fun onBoutonClicked() {
            _navigation.value = gestionNavigation.EN_ATTENTE
        }

        fun onClickButtonCreationOrModificationEnded() {

            Timber.i("Chantier ready to save in DB = ${rapportChantier.value?.chantierId}")
            if (rapportChantier.value?.chantierId == null) sendNewDataToDB()
            else updateDataInDB()

            _navigation.value = gestionNavigation.ENREGISTREMENT_CHANTIER
        }

        private fun updateDataInDB() {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    dataSource.update(rapportChantier.value!!)
                }
            }
        }

        private fun sendNewDataToDB() {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    dataSource.insert(rapportChantier.value!!)
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

    }
