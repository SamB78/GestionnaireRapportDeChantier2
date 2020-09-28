package com.example.gestionnairerapportdechantier.materiel.listeMateriel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.databinding.FragmentListeMaterielBinding
import timber.log.Timber


class ListeMaterielFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentListeMaterielBinding.inflate(inflater, container, false)
        binding.executePendingBindings()

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = GestionnaireDatabase.getInstance(application).materielDao
        val viewModelFactory =
            ListeMaterielViewModelFactory(
                dataSource
            )

        //viewModel
        val viewModel: ListeMaterielViewModel by activityViewModels { viewModelFactory }
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //RecyclerView
        val adapter = listeMaterielAdapter(
            ListeMaterielListener { materielId ->
                viewModel.onMaterielClicked(materielId.toLong())
            }
        )
        binding.materielListe.adapter = adapter

        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.materielListe.layoutManager = manager

        //Affichage liste Materiel dans le RecyclerView
        viewModel.listeMateriel.observe(viewLifecycleOwner, Observer { listeMateriel ->
            listeMateriel?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigationMateriel.observe(viewLifecycleOwner, Observer { navigation ->

            when (navigation) {
                ListeMaterielViewModel.navigationMenuMateriel.CREATION_MATERIEL -> {
                    Timber.i("Passage creation Materiel")
                    val action =
                        ListeMaterielFragmentDirections.actionListeMaterielFragmentToGestionMaterielFragment()
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
                ListeMaterielViewModel.navigationMenuMateriel.MODIFICATION_MATERIEL -> {
                    val action =
                        ListeMaterielFragmentDirections.actionListeMaterielFragmentToGestionMaterielFragment(
                            viewModel.idMateriel.value!!
                        )
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
            }
        })

        return binding.root
    }
}