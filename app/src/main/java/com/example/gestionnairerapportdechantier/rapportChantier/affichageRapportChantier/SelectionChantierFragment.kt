package com.example.gestionnairerapportdechantier.rapportChantier.affichageRapportChantier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.AffichageChantierFragment
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.AffichageChantierViewModel
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.databinding.FragmentAffichageDetailsRapportChantierBinding
import com.example.gestionnairerapportdechantier.databinding.FragmentSelectionChantierBinding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierFragmentArgs
import com.google.android.material.datepicker.MaterialDatePicker
import timber.log.Timber


class SelectionChantierFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSourceRapportChantier =
            GestionnaireDatabase.getInstance(application).rapportChantierDao
        val dataSourceChantier = GestionnaireDatabase.getInstance(application).chantierDao
        val dataSourceMateriel = GestionnaireDatabase.getInstance(application).materielDao
        val dataSourceMaterielLocation =
            GestionnaireDatabase.getInstance(application).materielLocationDao
        val dataSourceMateriaux = GestionnaireDatabase.getInstance(application).materiauxDao
        val dataSourceSousTraitance = GestionnaireDatabase.getInstance(application).sousTraitanceDao
        val idRapportChantier =
            GestionRapportChantierFragmentArgs.fromBundle(arguments!!).idRapportChantier
        val dataSourceAssociationPersonnelChantier =
            GestionnaireDatabase.getInstance(application).associationPersonnelChantierDao
        val dataSourcePersonnel = GestionnaireDatabase.getInstance(application).personnelDao
        val dataSourceAssociationPersonnelRapportChantier =
            GestionnaireDatabase.getInstance(application).associationPersonnelRapportChantierDao
        val dataSourceAssociationMaterielRapportChantierDao =
            GestionnaireDatabase.getInstance(application).associationMaterielRapportChantierDao
        val dataSourceAssociationMaterielLocationRapportChantierDao =
            GestionnaireDatabase.getInstance(application).associationMaterielLocationRapportChantierDao
        val dataSourceAssociationMateriauxRapportChantierDao =
            GestionnaireDatabase.getInstance(application).associationMateriauxRapportChantierDao
        val dataSourceAssociationSousTraitanceRapportChantierDao =
            GestionnaireDatabase.getInstance(application).associationSousTraitanceRapportChantierDao
        val idChantier = GestionRapportChantierFragmentArgs.fromBundle(arguments!!).idChantier
        val dateDebutRapportChantier =
            GestionRapportChantierFragmentArgs.fromBundle(arguments!!).date

        val viewModelFactory = AffichageDetailsRapportChanterViewModelFactory(
            application,
            dataSourceRapportChantier,
            dataSourceChantier,
            dataSourcePersonnel,
            dataSourceMateriel,
            dataSourceMaterielLocation,
            dataSourceMateriaux,
            dataSourceSousTraitance,
            dataSourceAssociationPersonnelChantier,
            dataSourceAssociationPersonnelRapportChantier,
            dataSourceAssociationMaterielRapportChantierDao,
            dataSourceAssociationMaterielLocationRapportChantierDao,
            dataSourceAssociationMateriauxRapportChantierDao,
            dataSourceAssociationSousTraitanceRapportChantierDao,
            idRapportChantier,
            idChantier,
            dateDebutRapportChantier,
            -1L
        )

        val viewModel: AffichageDetailsRapportChantierViewModel by navGraphViewModels(R.id.AffichageRapportsChantierNavGraph) { viewModelFactory }
        // Inflate the layout for this fragment

        val binding =
            FragmentSelectionChantierBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()


        // Create the date picker builder and set the title, and create the date picker
        val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
        // set listener when date is selected
        datePicker.addOnPositiveButtonClickListener {
            viewModel.onDatesSelected(it.first!!, it.second!!)
        }

        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->

            when (navigation) {
                AffichageDetailsRapportChantierViewModel.GestionNavigation.SELECTION_DATE -> {
                    datePicker.show(activity?.supportFragmentManager!!, "MyTAG")
                    viewModel.onBoutonClicked()

                }
                AffichageDetailsRapportChantierViewModel.GestionNavigation.DONNEES_SELECTIONNEES -> {
                    val action =
                        SelectionChantierFragmentDirections.actionSelectionChantierFragmentToAffichageDetailsRapportChantierFragment(
                            -1L
                        )
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()

                }
            }
        }
        )

        return binding.root
    }


}