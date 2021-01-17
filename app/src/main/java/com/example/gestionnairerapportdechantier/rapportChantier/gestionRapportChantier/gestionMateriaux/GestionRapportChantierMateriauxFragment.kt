package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriaux

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.DialogAddMateriauxBinding
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierMateriauxBinding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierViewModel
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionMateriel.gestionRapportChantierMaterielFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber


class GestionRapportChantierMateriauxFragment : Fragment() {


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
            viewModel.onClickButtonAddMateriaux()
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

        val binding = FragmentGestionRapportChantierMateriauxBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()


        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_AJOUT_MATERIAUX -> {
                    val customLayout = DialogAddMateriauxBinding.inflate(inflater)
                    customLayout.viewModel = viewModel

                    MaterialAlertDialogBuilder(context)
                        .setTitle("Ajouter des Matériaux")
                        .setView(customLayout.root)
                        .setNegativeButton("Annuler") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Valider") { dialog, which ->
                            viewModel.onClickButtonConfirmationAddMateriaux()
                            dialog.dismiss()
                        }
                        .show()


                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.VALIDATION_GESTION_MATERIAUX -> {
                    Timber.i("VALIDATION_GESTION_PERSONNEL")
                    val action =
                        GestionRapportChantierMateriauxFragmentDirections.actionGestionRapportChantierMateriauxFragmentToGestionRapportChantierFragment(
                            -1L, -1L
                        )
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
            }

        })
        return binding.root
    }
}