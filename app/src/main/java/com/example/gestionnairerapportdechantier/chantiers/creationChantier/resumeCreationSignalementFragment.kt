package com.example.gestionnairerapportdechantier.chantiers.creationChantier

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentResumeCreationSignalementBinding
import com.example.gestionnairerapportdechantier.personnel.listePersonnel.ListePersonnelAdapter
import com.example.gestionnairerapportdechantier.personnel.listePersonnel.ListePersonnelListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber


class resumeCreationSignalementFragment : Fragment() {

    //ViewModel
    val viewModel: CreationChantierViewModel by navGraphViewModels(R.id.creationChantierNavGraph)

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite->{
            MaterialAlertDialogBuilder(context)
                .setTitle("Annulation")
                .setMessage("Souhaitez vous annuler la création du nouveau chantier ?")
                .setNegativeButton("QUITTER") { dialog, which ->
                    // Respond to negative button press
                    viewModel.onClickButtonAnnuler()
                }
                .setPositiveButton("CONTINUER") { dialog, which ->
                    // Respond to positive button press
                    dialog.dismiss()
                }
                .show()
            true
        }else-> {
            super.onOptionsItemSelected(item)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentResumeCreationSignalementBinding.inflate(inflater, container, false)
        binding.executePendingBindings()

        //ViewModel Binding
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //RecyclerView
        val adapter = ListePersonnelAdapter(
            ListePersonnelListener { personnelId ->
                Toast.makeText(context, "$personnelId", Toast.LENGTH_SHORT).show()
            })

        binding.personnelListeResume.adapter = adapter
        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.personnelListeResume.layoutManager = manager

        viewModel._listePersonnelChantierValide.observe(viewLifecycleOwner, Observer { listePersonnel ->
            listePersonnel?.let {
                adapter.submitList(it)
                Timber.i("valeurs listePersonnelChantier: $it")
            }
        })



        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation){
                CreationChantierViewModel.gestionNavigation.ENREGISTREMENT_CHANTIER->{
                    findNavController().navigate(R.id.action_resumeCreationSignalementFragment_to_listeChantiersFragment)
                    viewModel.onBoutonClicked()
                }
                CreationChantierViewModel.gestionNavigation.MODIFICATION->{
                    findNavController().navigate(R.id.action_resumeCreationSignalementFragment_to_creationChantier1Fragment)
                    viewModel.onBoutonClicked()
                }
                CreationChantierViewModel.gestionNavigation.ANNULATION->{
                    findNavController().navigate(R.id.action_resumeCreationSignalementFragment_to_listeChantiersFragment)
                    viewModel.onBoutonClicked()
                }

            }
        })

        return binding.root
    }

}
