package com.example.gestionnairerapportdechantier.chantiers.affichageChantier

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.CreationChantierNavGraphDirections
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.detailAffichageChantier.DetailAffichageChantierFragment
import com.example.gestionnairerapportdechantier.databinding.FragmentAffichageChantierBinding
import com.example.gestionnairerapportdechantier.rapportChantier.listeRapportsChantier.listeRapportsChantierFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayoutMediator

/**
 * A simple [Fragment] subclass.
 */
class AffichageChantierFragment : Fragment() {

    private val fragmentList = arrayListOf(
        DetailAffichageChantierFragment(),
        listeRapportsChantierFragment()
    )

    private lateinit var viewModelFactory: AffichageChantierViewModelFactory
    val viewModel: AffichageChantierViewModel by navGraphViewModels(R.id.AffichageChantierNavGraph) { viewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_chantier, menu)
    }

    //Menu Option
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_export -> {
            viewModel.onClickButtonExportData()
            true
        }
        R.id.action_edit -> {
            viewModel.onClickButtonEditChantier()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSourceChantier = GestionnaireDatabase.getInstance(application).chantierDao
        val dataSourceAssociationPersonnelChantier =
            GestionnaireDatabase.getInstance(application).associationPersonnelChantierDao
        val dataSourcePersonnel = GestionnaireDatabase.getInstance(application).personnelDao
        val dataSourceRapportChantier =
            GestionnaireDatabase.getInstance(application).rapportChantierDao
        val chantierId = AffichageChantierFragmentArgs.fromBundle(arguments!!).idChantier

        viewModelFactory = AffichageChantierViewModelFactory(
            dataSourceChantier,
            dataSourceAssociationPersonnelChantier,
            dataSourcePersonnel,
            dataSourceRapportChantier,
            chantierId
        )

        val binding = FragmentAffichageChantierBinding.inflate(inflater)
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Create the date picker builder and set the title, and create the date picker
        val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
        // set listener when date is selected
        datePicker.addOnPositiveButtonClickListener {
            viewModel.onDatesToExportSelected(it.first!!, it.second!!)
        }

        val tabsAffichageChantierAdapter = AffichageChantierViewerPagerAdapter(this, fragmentList)
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

        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->

            when (navigation) {
                AffichageChantierViewModel.navigationMenu.SELECTION_DATE_EXPORT -> {
                    datePicker.show(activity?.supportFragmentManager!!, "MyTAG")
                    viewModel.onBoutonClicked()

                }
                AffichageChantierViewModel.navigationMenu.EXPORT -> {
                    val action =
                        AffichageChantierFragmentDirections.actionAffichageChantierFragmentToAffichageRapportsChantierNavGraph(
                            viewModel.chantier.value!!.chantierId!!.toLong(),
                            viewModel.dateDebut.value!!,
                            viewModel.dateFin.value!!
                        )
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()

                }
                AffichageChantierViewModel.navigationMenu.EDIT ->{

                    val action = CreationChantierNavGraphDirections.actionGlobalCreationChantierNavGraph(viewModel.idChantier)
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
            }
        }
        )

        return binding.root
    }

    override fun onResume() {
        viewModel.onResumeAfterEditChantier()
        super.onResume()
    }
}
