package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.observations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.AffichageChantierViewModel
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierPersonnelBinding
import com.example.gestionnairerapportdechantier.databinding.FragmentObservationsRapportChantierBinding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierViewModel

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



        // Inflate the layout for this fragment
        return binding.root
    }

}