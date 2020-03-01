package com.example.gestionnairerapportdechantier.personnel.listePersonnel

import androidx.lifecycle.*
import com.example.gestionnairerapportdechantier.Database.PersonnelDao
import com.example.gestionnairerapportdechantier.entities.Personnel
import kotlinx.coroutines.*
import timber.log.Timber


class ListePersonnelViewModel(private val dataSource: PersonnelDao): ViewModel(){


    enum class navigationMenuPersonnel {
        CREATION_PERSONNEL,
        MODIFICATION_PERSONNEL,
        EN_ATTENTE
    }

    //Coroutines ?
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val listePersonnel = dataSource.getAllFromPersonnel()

    private var _navigationPersonnel = MutableLiveData<navigationMenuPersonnel>()
    val navigationPersonnel: LiveData<navigationMenuPersonnel>
        get() = this._navigationPersonnel

    private var _idPersonnel = MutableLiveData<Long>()
    val idPersonnel: LiveData<Long>
        get() = this._idPersonnel

    init{
        onBoutonClicked()
    }

    fun onClickBoutonAjoutPersonnel(){
        _navigationPersonnel.value =
            navigationMenuPersonnel.CREATION_PERSONNEL
    }

    fun onBoutonClicked() {
        _navigationPersonnel.value =
            navigationMenuPersonnel.EN_ATTENTE
    }

    fun onPersonnelClicked(id: Long){
        _idPersonnel.value = id
        _navigationPersonnel.value = navigationMenuPersonnel.MODIFICATION_PERSONNEL
    }

    // onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}