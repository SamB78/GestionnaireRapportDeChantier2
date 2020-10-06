package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriel.ajoutMateriel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierAjoutMaterielBinding


class GestionRapportChantierAjoutMaterielFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGestionRapportChantierAjoutMaterielBinding.inflate(inflater)
//        binding.executePendingBindings()

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val rapportChantierId: Long = 1
        val dataSourceMateriel = GestionnaireDatabase.getInstance(application).materielDao
        val dataSourceAssociationMaterielRapportChantier =
            GestionnaireDatabase.getInstance(application).associationMaterielRapportChantierDao
        val viewModelFactory = GestionRapportChantierAjoutMaterielViewModelFactory(
            rapportChantierId,
            dataSourceMateriel,
            dataSourceAssociationMaterielRapportChantier
        )

        //ViewModel
        val viewModel: GestionRapportChantierAjoutMaterielViewModel by activityViewModels { viewModelFactory }
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Inflate the layout for this fragment
        return binding.root
    }
}