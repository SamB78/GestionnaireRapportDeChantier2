package com.example.gestionnairerapportdechantier.mainMenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainMenuViewModel: ViewModel() {

    enum class navigationMainMenu{
        EN_ATTENTE,
        PASSAGE_PAGE_PERSONNEL,
        PASSAGE_PAGE_CHANTIERS,
        PASSAGE_PAGE_RAPPORTS_CHANTIER
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

    fun onBoutonClicked(){
        _navigation.value = navigationMainMenu.EN_ATTENTE
    }



}