package com.example.gestionnairerapportdechantier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {


    enum class navigationMainMenu{
        EN_ATTENTE,
        PASSAGE_PAGE_PERSONNEL,
        PASSAGE_PAGE_CHANTIERS,
        PASSAGE_PAGE_RAPPORTS_CHANTIER,
        PASSAGE_PAGE_VEHICULES,
        PASSAGE_PAGE_MATERIEL
    }

    private var _navigation = MutableLiveData<navigationMainMenu>()
    val navigation: LiveData<navigationMainMenu>
        get() = this._navigation

    init {
        onBoutonClicked()
    }

    fun onClickBoutonPersonnel(){
        _navigation.value = navigationMainMenu.PASSAGE_PAGE_PERSONNEL
    }

    fun onClickBoutonChantier(){
        _navigation.value = navigationMainMenu.PASSAGE_PAGE_CHANTIERS
    }

    fun onClickBoutonRapportsChantier(){
        _navigation.value = navigationMainMenu.PASSAGE_PAGE_RAPPORTS_CHANTIER
    }

    fun onClickButtonVehicules(){
        _navigation.value = navigationMainMenu.PASSAGE_PAGE_VEHICULES
    }

    fun onClickButtonMateriel(){
        _navigation.value = navigationMainMenu.PASSAGE_PAGE_MATERIEL
    }

    fun onBoutonClicked(){
        _navigation.value = navigationMainMenu.EN_ATTENTE
    }
}