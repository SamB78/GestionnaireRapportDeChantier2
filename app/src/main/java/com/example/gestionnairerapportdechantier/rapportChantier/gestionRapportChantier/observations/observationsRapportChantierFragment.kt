package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.observations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.MainActivity
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.AffichageChantierViewModel
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierPersonnelBinding
import com.example.gestionnairerapportdechantier.databinding.FragmentObservationsRapportChantierBinding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierViewModel
import com.example.gestionnairerapportdechantier.utils.hideKeyboard

class observationsRapportChantierFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentObservationsRapportChantierBinding.inflate(inflater, container, false)
        binding.executePendingBindings()
        //ViewModel
        val viewModel: GestionRapportChantierViewModel by navGraphViewModels(R.id.gestionRapportChantiers)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //Navigation

        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            hideKeyboard(activity as MainActivity)
            when (navigation) {

                GestionRapportChantierViewModel.GestionNavigation.VALIDATION_OBSERVATIONS -> {
                    val action =
                        observationsRapportChantierFragmentDirections.actionObservationsRapportChantierToGestionRapportChantierFragment(
                            -1L,
                            -1L
                        )
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
            }

        })


        // Inflate the layout for this fragment
        return binding.root
    }

}