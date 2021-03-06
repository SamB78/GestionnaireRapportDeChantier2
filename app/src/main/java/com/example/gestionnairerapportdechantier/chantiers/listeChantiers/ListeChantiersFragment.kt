package com.example.gestionnairerapportdechantier.chantiers.listeChantiers


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gestionnairerapportdechantier.CreationChantierNavGraphDirections
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentListeChantiersBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ListeChantiersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentListeChantiersBinding.inflate(inflater, container, false)
        binding.executePendingBindings()

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSourceChantier = GestionnaireDatabase.getInstance(application).chantierDao
        val dataSourceAssociationPersonnelChantier = GestionnaireDatabase.getInstance(application).associationPersonnelChantierDao
        val viewModelFactory =
            ListeChantiersViewModelFactory(
                dataSourceChantier,
                dataSourceAssociationPersonnelChantier
            )

        //ViewModel
        val viewModel: ListeChantiersViewModel by activityViewModels { viewModelFactory }
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //RecyclerView

        val adapter =
            ListeChantierAdapter(
                ListeChantierListener{ chantierId ->
                    Toast.makeText(context, "$chantierId", Toast.LENGTH_SHORT).show()
                    viewModel.onChantierClicked(chantierId.toLong())
                })
        binding.personnelListe.adapter = adapter

        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.personnelListe.layoutManager = manager

        viewModel.listeChantiers.observe(viewLifecycleOwner, Observer { listeChantiers ->
            listeChantiers?.let {
                adapter.submitList(it)
                Timber.i("Chantiers: $it")
            }
        })


        //Navigation

        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                ListeChantiersViewModel.navigationMenu.CREATION -> {
                    Toast.makeText(activity, "Passage creation Chantier", Toast.LENGTH_SHORT)
                        .show()

                    val action = CreationChantierNavGraphDirections.actionGlobalCreationChantierNavGraph()
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
                ListeChantiersViewModel.navigationMenu.MODIFICATION -> {
                    Toast.makeText(activity, "Passage modification Chantier", Toast.LENGTH_SHORT)
                        .show()

                    val action = ListeChantiersFragmentDirections.actionListeChantiersFragmentToAffichageChantierNavGraph(viewModel.idChantier.value!!)



                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
            }
        })

        return binding.root
    }


}
