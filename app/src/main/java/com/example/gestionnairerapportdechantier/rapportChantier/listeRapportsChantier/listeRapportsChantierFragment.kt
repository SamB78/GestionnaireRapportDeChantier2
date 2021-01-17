package com.example.gestionnairerapportdechantier.rapportChantier.listeRapportsChantier


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.AffichageChantierFragmentDirections
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.AffichageChantierViewModel
import com.example.gestionnairerapportdechantier.databinding.FragmentListeRapportsChantierBinding
import com.google.android.material.datepicker.MaterialDatePicker
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class listeRapportsChantierFragment : Fragment() {

    //ViewModel
    val viewModel: AffichageChantierViewModel by navGraphViewModels(R.id.AffichageChantierNavGraph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentListeRapportsChantierBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()

        // Calendrier
        var date = ""
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd =
            context?.let {
                DatePickerDialog(it, DatePickerDialog.OnDateSetListener { view, year, month, day ->

                    // Display Selected date in textbox
                    Timber.i("DATE:  $day  $month $year")
                    val localDate = LocalDate.of(year, month, day)
                    date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    Timber.i("Date pre VM = $date")
                    viewModel.onDateSelected()


                }, year, month, day)
            }
        dpd?.datePicker?.maxDate = System.currentTimeMillis()


        // Create the date picker builder and set the title
        val builder = MaterialDatePicker.Builder.datePicker()
        // create the date picker
        val datePicker = builder.build()
        var date2: Long = 0
        // set listener when date is selected
        datePicker.addOnPositiveButtonClickListener {

            // Create calendar object and set the date to be that returned from selectio
            date2 = it
            viewModel.onDateSelected()
        }


        //navigation
        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                AffichageChantierViewModel.navigationMenu.CREATION -> {
                    Timber.i("Date PENDANT CREATION = $date")

                    if (findNavController().currentDestination?.id == R.id.listeRapportsChantierFragment) {
                        val action =
                            listeRapportsChantierFragmentDirections.actionListeRapportsChantierFragmentToGestionRapportChantiers(
                                -1L,
                                date2,
                                viewModel.chantier.value?.chantierId!!
                            )
                        findNavController().navigate(action)
                    } else {
                        val action =
                            AffichageChantierFragmentDirections.actionAffichageChantierFragmentToGestionRapportChantiers(
                                -1L,
                                date2,
                                viewModel.chantier.value?.chantierId!!
                            )
                        findNavController().navigate(action)

                    }
                    viewModel.onBoutonClicked()
                }
                AffichageChantierViewModel.navigationMenu.MODIFICATION_RAPPORT_CHANTIER -> {
                    Timber.i("Date PENDANT CREATION = $date")

                    if (findNavController().currentDestination?.id == R.id.listeRapportsChantierFragment) {
                        val action =
                            listeRapportsChantierFragmentDirections.actionListeRapportsChantierFragmentToGestionRapportChantiers(
                                viewModel.idRapportChantier.value!!, -1L
                            )
                        findNavController().navigate(action)
                    } else {
                        val action =
                            AffichageChantierFragmentDirections.actionAffichageChantierFragmentToGestionRapportChantiers(
                                viewModel.idRapportChantier.value!!, -1L
                            )
                        findNavController().navigate(action)

                    }
                    viewModel.onBoutonClicked()
                }
//                AffichageChantierViewModel.navigationMenu.CONSULTATION ->{
//                    if (findNavController().currentDestination?.id == R.id.listeRapportsChantierFragment) {
//                        val action =
//                            listeRapportsChantierFragmentDirections.actionListeRapportsChantierFragmentToAffichageDetailsRapportChantierFragment(
//                                viewModel.idRapportChantier.value!!
//                            )
//                        findNavController().navigate(action)
//                    } else {
//                        val action =
//                            AffichageChantierFragmentDirections.actionAffichageChantierFragmentToAffichageDetailsRapportChantierFragment(
//                                viewModel.idRapportChantier.value!!
//                            )
//                        findNavController().navigate(action)
//
//                    }
//                    viewModel.onBoutonClicked()
//
//                }

                AffichageChantierViewModel.navigationMenu.SELECTION_DATE -> {
                    viewModel.onBoutonClicked()
                    datePicker.show(activity?.supportFragmentManager!!, "MyTAG")
//                    dpd?.show()
                }
                else -> {
                    Timber.i("Nothing")
                }
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume()")
        viewModel.onResumeGestionMaterielFragment()
    }

}


//RecyclerView

//        val adapter =
//            ListeRapportChantiersAdapter(
//                ListeRapportChantiersListener { rapportChantierId ->
//                    viewModel.onRapportChantierClicked(rapportChantierId.toLong())
//                    Timber.i("test")
//                }
//            )
//        binding.rapportChantiersListe.adapter = adapter
//
//        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
//        binding.rapportChantiersListe.layoutManager = manager
//
//        viewModel.listeRapportsChantiers.observe(
//            viewLifecycleOwner,
//            Observer { listeRapportsChantiers ->
//                listeRapportsChantiers?.let {
//                    adapter.submitList(it)
//                    Timber.i("Rapports de chantiers: $it")
//                }
//            })

