package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierMaterielBinding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierViewModel


class gestionRapportChantierMaterielFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =
            FragmentGestionRapportChantierMaterielBinding.inflate(inflater, container, false)
//        binding.executePendingBindings()

        //ViewModel
        val viewModel: GestionRapportChantierViewModel by navGraphViewModels(R.id.gestionRapportChantiers)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //Navigation

        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_AJOUT_MATERIEL -> {
                    val action =
                        gestionRapportChantierMaterielFragmentDirections.actionGestionRapportChantierMaterielFragmentToGestionRapportChantierAjoutMaterielFragment(
                            viewModel.rapportChantier.value?.rapportChantierId!!.toLong()
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