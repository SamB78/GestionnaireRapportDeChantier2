package com.example.gestionnairerapportdechantier.chantiers.affichageChantier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.Database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.detailAffichageChantier.DetailAffichageChantierFragment
import com.example.gestionnairerapportdechantier.databinding.FragmentAffichageChantierBinding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * A simple [Fragment] subclass.
 */
class AffichageChantierFragment : Fragment() {

    private val fragmentList = arrayListOf(
        DetailAffichageChantierFragment(),
        GestionRapportChantierFragment()
    )

    private lateinit var viewModelFactory: AffichageChantierViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAffichageChantierBinding.inflate(inflater)
        binding.executePendingBindings()

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSourceChantier = GestionnaireDatabase.getInstance(application).ChantierDao
        val dataSourceAssociationPersonnelChantier =
            GestionnaireDatabase.getInstance(application).AssociationPersonnelChantierDao
        val dataSourcePersonnel = GestionnaireDatabase.getInstance(application).PersonnelDao
        val chantierId = AffichageChantierFragmentArgs.fromBundle(arguments!!).idChantier

        viewModelFactory = AffichageChantierViewModelFactory(
            dataSourceChantier,
            dataSourceAssociationPersonnelChantier,
            dataSourcePersonnel,
            chantierId
        )

        //viewModel
        val viewModel: AffichageChantierViewModel by navGraphViewModels(R.id.AffichageChantierNavGraph){ viewModelFactory}
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var tabsAffichageChantierAdapter = AffichageChantierViewerPagerAdapter(this, fragmentList)
        binding.pager.adapter = tabsAffichageChantierAdapter

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "INFORMATIONS"

                }
                1 -> {
                    tab.text = "RAPPORTS DE CHANTIER"
                }
            }
        }.attach()

        return binding.root
    }
}
