package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMaterielLocation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.DialogAddMateriauxBinding
import com.example.gestionnairerapportdechantier.databinding.DialogAddMaterielLocationBinding
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierMaterielBinding
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierMaterielLocationBinding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierViewModel
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriaux.GestionRapportChantierMateriauxFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber


class GestionRapportChantierMaterielLocationFragment : Fragment() {

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
            viewModel.onClickButtonAddMaterielLocation()
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
            FragmentGestionRapportChantierMaterielLocationBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()

        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_AJOUT_MATERIEL_LOCATION -> {
                    val customLayout = DialogAddMaterielLocationBinding.inflate(inflater)
                    customLayout.viewModel = viewModel

                    MaterialAlertDialogBuilder(context)
                        .setTitle("Ajouter un matériel de location")
                        .setView(customLayout.root)
                        .setNegativeButton("Annuler") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Valider") { dialog, which ->
                            viewModel.onClickButtonConfirmationAddMaterielLocation()
                            dialog.dismiss()
                        }
                        .show()


                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.VALIDATION_GESTION_MATERIEL_LOCATION -> {
                    Timber.i("VALIDATION_GESTION_PERSONNEL")
                    val action =
                        GestionRapportChantierMaterielLocationFragmentDirections.actionGestionRapportChantierMaterielLocationFragmentToGestionRapportChantierFragment(
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


}