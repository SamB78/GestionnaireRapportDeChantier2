package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val binding = FragmentGestionRapportChantierBinding.inflate(inflater)
        binding.executePendingBindings()

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSourceRapportChantier =
            GestionnaireDatabase.getInstance(application).rapportChantierDao
        val dataSourceChantier = GestionnaireDatabase.getInstance(application).chantierDao
        val dataSourceMateriel= GestionnaireDatabase.getInstance(application).materielDao
        val idRapportChantier =
            GestionRapportChantierFragmentArgs.fromBundle(arguments!!).idRapportChantier
        val dataSourceAssociationPersonnelChantier =
            GestionnaireDatabase.getInstance(application).associationPersonnelChantierDao
        val dataSourcePersonnel = GestionnaireDatabase.getInstance(application).personnelDao
        val dataSourceAssociationPersonnelRapportChantier = GestionnaireDatabase.getInstance(application).associationPersonnelRapportChantierDao
        val dataSourceAssociationMaterielRapportChantierDao = GestionnaireDatabase.getInstance(application).associationMaterielRapportChantierDao
        val idChantier = GestionRapportChantierFragmentArgs.fromBundle(arguments!!).idChantier
        val dateRapportChantier = GestionRapportChantierFragmentArgs.fromBundle(arguments!!).date

        val viewModelFactory = GestionRapportChantierViewModelFactory(
            dataSourceRapportChantier,
            dataSourceChantier,
            dataSourcePersonnel,
            dataSourceMateriel,
            dataSourceAssociationPersonnelChantier,
            dataSourceAssociationPersonnelRapportChantier,
            dataSourceAssociationMaterielRapportChantierDao,
            idRapportChantier,
            idChantier,
            dateRapportChantier
        )

        //ViewModel
        val viewModel: GestionRapportChantierViewModel by navGraphViewModels(R.id.gestionRapportChantiers) { viewModelFactory }
        binding.lifecycleOwner = this
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
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_GESTION_MATERIEL-> {
                    findNavController().navigate(R.id.action_gestionRapportChantierFragment_to_gestionRapportChantierMaterielFragment)
                    viewModel.onBoutonClicked()
                }
            }
        })

        return binding.root
    }


}
