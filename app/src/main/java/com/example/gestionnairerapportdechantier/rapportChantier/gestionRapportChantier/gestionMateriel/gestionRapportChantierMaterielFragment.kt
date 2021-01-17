package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriel

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierMaterielBinding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierViewModel
import timber.log.Timber


class gestionRapportChantierMaterielFragment : Fragment() {

    //ViewModel
    val viewModel: GestionRapportChantierViewModel by navGraphViewModels(R.id.gestionRapportChantiers)

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gestion_materiel, menu)
    }

    //Menu Option
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add_materiel -> {
            viewModel.onClickButtonAddMateriel()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding =
            FragmentGestionRapportChantierMaterielBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()

        //Navigation

        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_AJOUT_MATERIEL -> {
                    Timber.i("rapportChantierId in fragment: ${viewModel.rapportChantier.value?.rapportChantierId} ")
                    val action =
                        gestionRapportChantierMaterielFragmentDirections.actionGestionRapportChantierMaterielFragmentToGestionRapportChantierAjoutMaterielFragment(
                            viewModel.rapportChantier.value?.rapportChantierId!!.toLong()
                        )
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.VALIDATION_GESTION_MATERIEL -> {
                    Timber.i("VALIDATION_GESTION_PERSONNEL")
                    val action =
                        gestionRapportChantierMaterielFragmentDirections.actionGestionRapportChantierMaterielFragmentToGestionRapportChantierFragment(
                            -1L, -1L
                        )
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
            }

        })

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResumeGestionMaterielFragment()
    }
}