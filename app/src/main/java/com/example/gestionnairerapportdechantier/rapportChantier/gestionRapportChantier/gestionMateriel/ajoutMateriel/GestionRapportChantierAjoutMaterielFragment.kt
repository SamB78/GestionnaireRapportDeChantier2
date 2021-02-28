package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriel.ajoutMateriel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierAjoutMaterielBinding
import timber.log.Timber


class GestionRapportChantierAjoutMaterielFragment : Fragment() {

    private lateinit var viewModelFactory: GestionRapportChantierAjoutMaterielViewModelFactory
    val viewModel: GestionRapportChantierAjoutMaterielViewModel by viewModels { viewModelFactory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val rapportChantierId: Long =
            GestionRapportChantierAjoutMaterielFragmentArgs.fromBundle(arguments!!).idRapportChantier
        val dataSourceMateriel = GestionnaireDatabase.getInstance(application).materielDao
        val dataSourceAssociationMaterielRapportChantier =
            GestionnaireDatabase.getInstance(application).associationMaterielRapportChantierDao
        val dataSourceRapportChantierDao = GestionnaireDatabase.getInstance(application).rapportChantierDao
        viewModelFactory = GestionRapportChantierAjoutMaterielViewModelFactory(
            rapportChantierId,
            dataSourceMateriel,
            dataSourceAssociationMaterielRapportChantier,
            dataSourceRapportChantierDao
        )

        Timber.i("rapportChantierId in fragment: $rapportChantierId ")

        //ViewModel

        val binding =
            FragmentGestionRapportChantierAjoutMaterielBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()



        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionRapportChantierAjoutMaterielViewModel.navigationMenu.VALIDATION -> {
                    val action =
                        GestionRapportChantierAjoutMaterielFragmentDirections.actionGestionRapportChantierAjoutMaterielFragmentToGestionRapportChantierMaterielFragment()
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

}