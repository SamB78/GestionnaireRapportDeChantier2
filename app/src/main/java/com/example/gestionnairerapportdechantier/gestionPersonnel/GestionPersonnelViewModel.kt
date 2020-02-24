package com.example.gestionnairerapportdechantier.gestionPersonnel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestionnairerapportdechantier.Database.PersonnelDao
import com.example.gestionnairerapportdechantier.entities.Personnel


class GestionPersonnelViewModel(private val dataSource: PersonnelDao): ViewModel(){


    enum class navigationMenuPersonnel {
        CREATION_PERSONNEL,
        DETAIL_PERSONNEL,
        MODIFICATION_PERSONNEL,
        EN_ATTENTE
    }


    val listePersonnel = dataSource.getAllFromPersonnel()

    var newPersonnel = MutableLiveData<Personnel>()

    private var _navigationPersonnel = MutableLiveData<navigationMenuPersonnel>()
    val navigationPersonnel: LiveData<navigationMenuPersonnel>
        get() = this._navigationPersonnel

    init{
        onBoutonClicked()
    }


    fun onClickBoutonAjoutPersonnel(){
        _navigationPersonnel.value = navigationMenuPersonnel.CREATION_PERSONNEL
    }

    fun onBoutonClicked() {
        _navigationPersonnel.value = navigationMenuPersonnel.EN_ATTENTE
    }





}