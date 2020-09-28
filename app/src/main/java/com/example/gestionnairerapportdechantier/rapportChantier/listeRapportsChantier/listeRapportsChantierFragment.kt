package com.example.gestionnairerapportdechantier.rapportChantier.listeRapportsChantier


import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.AffichageChantierFragmentDirections
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.AffichageChantierViewModel
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.databinding.FragmentListeRapportsChantierBinding
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class listeRapportsChantierFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentListeRapportsChantierBinding.inflate(inflater)
        binding.executePendingBindings()


        //ViewModelFactory
//        val application = requireNotNull(this.activity).application
//        val dataSource = GestionnaireDatabase.getInstance(application).RapportChantierDao
//        val viewModelFactory = ListeRapportsChantierViewModelFactory(dataSource)

        //ViewModel
        val viewModel: AffichageChantierViewModel by navGraphViewModels(R.id.AffichageChantierNavGraph)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

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

        //RecyclerView

        val adapter =
            ListeRapportChantiersAdapter(
                ListeRapportChantiersListener { rapportChantierId ->
                    viewModel.onRapportChantierClicked(rapportChantierId.toLong())
                    Timber.i("test")
                }
            )
        binding.rapportChantiersListe.adapter = adapter

        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.rapportChantiersListe.layoutManager = manager

        viewModel.listeRapportsChantiers.observe(
            viewLifecycleOwner,
            Observer { listeRapportsChantiers ->
                listeRapportsChantiers?.let {
                    adapter.submitList(it)
                    Timber.i("Rapports de chantiers: $it")
                }
            })


        //navigation
        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                ListeRapportsChantierViewModel.navigationMenu.CREATION -> {
                    Timber.i("Date PENDANT CREATION = $date")

                    if (findNavController().currentDestination?.id == R.id.listeRapportsChantierFragment) {
                        val action =
                            listeRapportsChantierFragmentDirections.actionListeRapportsChantierFragmentToGestionRapportChantiers(
                                -1L,
                                date,
                                viewModel.chantier.value?.chantierId!!
                            )
                        findNavController().navigate(action)
                    } else {
                        val action =
                            AffichageChantierFragmentDirections.actionAffichageChantierFragmentToGestionRapportChantiers(
                                -1L,
                                date,
                                viewModel.chantier.value?.chantierId!!
                            )
                        findNavController().navigate(action)

                    }
                    viewModel.onBoutonClicked()
                }
                ListeRapportsChantierViewModel.navigationMenu.MODIFICATION -> {
                    Timber.i("Date PENDANT CREATION = $date")

                    if (findNavController().currentDestination?.id == R.id.listeRapportsChantierFragment) {
                        val action =
                            listeRapportsChantierFragmentDirections.actionListeRapportsChantierFragmentToGestionRapportChantiers(
                                viewModel.idRapportChantier.value!!, null
                            )
                        findNavController().navigate(action)
                    } else {
                        val action =
                            AffichageChantierFragmentDirections.actionAffichageChantierFragmentToGestionRapportChantiers(
                                viewModel.idRapportChantier.value!!, null
                            )
                        findNavController().navigate(action)

                    }
                    viewModel.onBoutonClicked()
                }


                ListeRapportsChantierViewModel.navigationMenu.SELECTION_DATE -> {
                    viewModel.onBoutonClicked()
                    dpd?.show()
                }
                else -> {
                    Timber.i("Nothing")
                }
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }


}

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(
            activity,
            this,
            hour,
            minute,
            DateFormat.is24HourFormat(activity)
        )
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
    }
}


