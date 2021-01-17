package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierBinding
import androidx.lifecycle.Observer
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class GestionRapportChantierFragment : Fragment() {

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
        val dateRapportChantier = GestionRapportChantierFragmentArgs.fromBundle(arguments!!).date

        val viewModelFactory = GestionRapportChantierViewModelFactory(
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
            dateRapportChantier
        )

        //ViewModel
        val viewModel: GestionRapportChantierViewModel by navGraphViewModels(R.id.gestionRapportChantiers) { viewModelFactory }
        val binding = FragmentGestionRapportChantierBinding.inflate(inflater, container, false)
//        val binding: FragmentGestionRapportChantierBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gestion_rapport_chantier, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        binding.viewModel = viewModel

        //Navigation
        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_GESTION_PERSONNEL -> {
                    Timber.i("PASSAGE_GESTION_PERSONNEL")
                    findNavController().navigate(R.id.action_gestionRapportChantierFragment_to_gestionRapportChantierPersonnelFragment)
                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_AUTRES_INFORMATIONS -> {
                    findNavController().navigate(R.id.action_gestionRapportChantierFragment_to_autresInformationsFragment)
                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_OBSERVATIONS -> {
                    findNavController().navigate(R.id.action_gestionRapportChantierFragment_to_observationsRapportChantier)
                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_GESTION_MATERIEL -> {
                    findNavController().navigate(R.id.action_gestionRapportChantierFragment_to_gestionRapportChantierMaterielFragment)
                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_GESTION_MATERIAUX -> {
                    findNavController().navigate(R.id.action_gestionRapportChantierFragment_to_gestionRapportChantierMateriauxFragment)
                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_GESTION_MATERIEL_LOCATION -> {
                    findNavController().navigate(R.id.action_gestionRapportChantierFragment_to_gestionRapportChantierMaterielLocationFragment)
                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_GESTION_SOUS_TRAITANCE -> {
                    findNavController().navigate(R.id.action_gestionRapportChantierFragment_to_gestionRapportChantierSousTraitanceFragment)
                    viewModel.onBoutonClicked()
                }

            }
        })

        return binding.root
    }


}
