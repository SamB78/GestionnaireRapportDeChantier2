package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionPersonnel

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierPersonnelBinding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierViewModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


class GestionRapportChantierPersonnelFragment : Fragment() {

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
            viewModel.onClickButtonAddPersonnel()
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
            FragmentGestionRapportChantierPersonnelBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()

//        //RecyclerView
//        val adapter = GestionHeuresPersonnelAdapter(
//            GestionHeuresPersonnelListener { personnelId ->
//                when (GestionHeuresPersonnelListener.click) {
//                    -1 -> {
//                        val onClickResult = viewModel.onClickMoinsHorairesTravailles(personnelId)
//                        if (!onClickResult) {
//                            Snackbar.make(
//                                view!!,
//                                "Vous êtes déjà à 0 heures travaillées",
//                                Snackbar.LENGTH_SHORT
//                            ).show()
//                        }
//
//                    }
//                    +1 -> {
//                        val onClickResult = viewModel.onClickPlusHorairesTravailles(personnelId)
//                        if (!onClickResult) {
//                            Snackbar.make(
//                                view!!,
//                                " Vous ne pouvez pas travailler plus de 8 heures !",
//                                Snackbar.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                    else -> {
//                        Toast.makeText(context, "$personnelId +0", Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        )
//        binding.personnelListe.adapter = adapter
//
//        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
//        binding.personnelListe.layoutManager = manager
//
//        // Affichage liste Personnel dans RecyclerView
//        viewModel.listePersonnelRapportChantier.observe(
//            viewLifecycleOwner,
//            Observer { listePersonnel ->
//                listePersonnel?.let { it ->
//                    Timber.i("Mise à jour de la liste")
//                    adapter.submitList(it)
//                    adapter.notifyDataSetChanged()
//                    listePersonnel.forEach {
//                        Timber.i("liste personnel: $it")
//                    }
//                }
//            })

        //Navigation
        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionRapportChantierViewModel.GestionNavigation.VALIDATION_GESTION_PERSONNEL -> {
                    val action =
                        GestionRapportChantierPersonnelFragmentDirections.actionGestionRapportChantierPersonnelFragmentToGestionRapportChantierFragment(
                            -1L, -1L
                        )
                    findNavController().navigate(action)

                    viewModel.onBoutonClicked()
                }
                GestionRapportChantierViewModel.GestionNavigation.PASSAGE_AJOUT_PERSONNEL -> {
                    val action = GestionRapportChantierPersonnelFragmentDirections.actionGestionRapportChantierPersonnelFragmentToGestionRapportChantierAjoutPersonnelFragment(
                        viewModel.rapportChantier.value!!.rapportChantierId!!.toLong()
                    )
                    findNavController().navigate(action)

                    viewModel.onBoutonClicked()
                }
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResumeGestionPersonnelFragment()
    }

}