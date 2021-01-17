package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionPersonnel.ajoutPersonnel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierAjoutPersonnelBinding


class GestionRapportChantierAjoutPersonnelFragment : Fragment() {

    private lateinit var viewModelFactory: GestionRapportChantierAjoutPersonnelViewModelFactory
    val viewModel: GestionRapportChantierAjoutPersonnelViewModel by viewModels { viewModelFactory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val rapportChantierId: Long =
            GestionRapportChantierAjoutPersonnelFragmentArgs.fromBundle(arguments!!).idRapportChantier
        val dataSourcePersonnel = GestionnaireDatabase.getInstance(application).personnelDao
        val dataSourceAssociationPersonnelRapportChantier =
            GestionnaireDatabase.getInstance(application).associationPersonnelRapportChantierDao
        viewModelFactory = GestionRapportChantierAjoutPersonnelViewModelFactory(
            rapportChantierId,
            dataSourcePersonnel,
            dataSourceAssociationPersonnelRapportChantier
        )

        val binding = FragmentGestionRapportChantierAjoutPersonnelBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()


        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionRapportChantierAjoutPersonnelViewModel.navigationMenu.VALIDATION -> {
                    val action =
                        GestionRapportChantierAjoutPersonnelFragmentDirections.actionGestionRapportChantierAjoutPersonnelFragmentToGestionRapportChantierPersonnelFragment()
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }
}