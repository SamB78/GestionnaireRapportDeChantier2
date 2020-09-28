package com.example.gestionnairerapportdechantier.vehicules.listeVehicules

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.databinding.FragmentListeVehiculesBinding


class ListeVehiculesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentListeVehiculesBinding.inflate(inflater, container, false)
        binding.executePendingBindings()

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = GestionnaireDatabase.getInstance(application).vehiculeDao
        val viewModelFactory =
            ListeVehiculesViewModelFactory(
                dataSource
            )

        //viewModel
        val viewModel: ListeVehiculesViewModel by activityViewModels { viewModelFactory }
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //RecyclerView
        val adapter = listeVehiculesAdapter(
            ListeVehiculeListener { vehiculeId ->
                viewModel.onVehiculeClicked(vehiculeId.toLong())
            }
        )
        binding.vehiculeListe.adapter = adapter

        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.vehiculeListe.layoutManager = manager

        //Affichage liste Vehicules dans le RecyclerView
        viewModel.listeVehicules.observe(viewLifecycleOwner, Observer { listeVehicules ->
            listeVehicules?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigationVehicules.observe(viewLifecycleOwner, Observer { navigation ->

            when (navigation) {
                ListeVehiculesViewModel.navigationMenuVehicules.CREATION_VEHICULE -> {
                    val action =
                        ListeVehiculesFragmentDirections.actionListeVehiculesFragmentToGestionVehiculeFragment()
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
                ListeVehiculesViewModel.navigationMenuVehicules.MODIFICATION_VEHICULE -> {
                    val action =
                        ListeVehiculesFragmentDirections.actionListeVehiculesFragmentToGestionVehiculeFragment(
                            viewModel.idVehicule.value!!
                        )
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
            }
        })

        return binding.root
    }
}