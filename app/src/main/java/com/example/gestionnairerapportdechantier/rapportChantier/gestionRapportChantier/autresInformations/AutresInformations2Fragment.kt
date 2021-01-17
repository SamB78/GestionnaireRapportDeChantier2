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
import com.example.gestionnairerapportdechantier.databinding.FragmentAutresInformations2Binding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierViewModel

class AutresInformations2Fragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAutresInformations2Binding.inflate( inflater, container, false)
        binding.executePendingBindings()

        //ViewModel
        val viewModel: GestionRapportChantierViewModel by navGraphViewModels(R.id.gestionRapportChantiers)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //Navigation
        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionRapportChantierViewModel.GestionNavigation.VALIDATION_AUTRES_INFORMATIONS -> {
//                    findNavController().navigate(R.id.action_autresInformations2Fragment_to_gestionRapportChantierFragment)

                    val action =
                        AutresInformations2FragmentDirections.actionAutresInformations2FragmentToGestionRapportChantierFragment(
                            -1L, -1L
                        )
                    findNavController().navigate(action)

                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.ANNULATION -> {
                    findNavController().navigate(R.id.action_autresInformations2Fragment_to_gestionRapportChantierFragment)
                }
            }
        })


        // Inflate the layout for this fragment
        return binding.root }

}