package com.example.gestionnairerapportdechantier.rapportChantier.listeRapportsChantier


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gestionnairerapportdechantier.Database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentListeRapportsChantierBinding

/**
 * A simple [Fragment] subclass.
 */
class listeRapportsChantierFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentListeRapportsChantierBinding.inflate(inflater)
        binding.executePendingBindings()


        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = GestionnaireDatabase.getInstance(application).RapportChantierDao
        val viewModelFactory = ListeRapportsChantierViewModelFactory(dataSource)

        //ViewModel
        val viewModel: ListeRapportsChantierViewModel by viewModels {viewModelFactory}
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //navigation

        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when(navigation){
                ListeRapportsChantierViewModel.navigationMenu.CREATION->{
                    findNavController().navigate(R.id.action_listeRapportsChantierFragment_to_gestionRapportChantierFragment)
                    viewModel.onBoutonClicked()
                }

            }
        })



        // Inflate the layout for this fragment
        return binding.root
    }


}
