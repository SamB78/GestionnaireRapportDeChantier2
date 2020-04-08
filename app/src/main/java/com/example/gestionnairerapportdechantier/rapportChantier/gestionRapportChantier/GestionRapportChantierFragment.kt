package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.gestionnairerapportdechantier.Database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierBinding

/**
 * A simple [Fragment] subclass.
 */
class GestionRapportChantierFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGestionRapportChantierBinding.inflate(inflater)
        binding.executePendingBindings()

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = GestionnaireDatabase.getInstance(application).RapportChantierDao
        val idRapportChantier = -1 as Long
        val viewModelFactory = GestionRapportChantierViewModelFactory(dataSource, idRapportChantier)

        //ViewModel
        val viewModel: GestionRapportChantierViewModel by viewModels { viewModelFactory}
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        return binding.root
    }


}
