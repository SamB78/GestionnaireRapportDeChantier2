package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.autresInformations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentAutresInformationsBinding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierViewModel
import timber.log.Timber


class AutresInformationsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAutresInformationsBinding.inflate(inflater, container, false)
        binding.executePendingBindings()

        //ViewModel
        val viewModel: GestionRapportChantierViewModel by navGraphViewModels(R.id.gestionRapportChantiers)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //Navigation
        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_ETAPE_2_AUTRES_INFORMATIONS -> {
                    findNavController().navigate(R.id.action_autresInformationsFragment_to_autresInformations2Fragment)
                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.ANNULATION -> {
                    findNavController().navigate(R.id.action_autresInformationsFragment_to_gestionRapportChantierFragment)
                }
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }

}