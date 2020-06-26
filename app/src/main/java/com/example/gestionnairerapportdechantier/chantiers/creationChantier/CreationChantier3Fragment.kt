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
import com.example.gestionnairerapportdechantier.databinding.FragmentCreationChantier3Binding
import com.example.gestionnairerapportdechantier.personnel.listePersonnel.ListePersonnelAdapter
import com.example.gestionnairerapportdechantier.personnel.listePersonnel.ListePersonnelListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_liste_chantiers.view.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class CreationChantier3Fragment : Fragment() {

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

        val binding = FragmentCreationChantier3Binding.inflate(inflater, container, false)
        binding.executePendingBindings()

        //ViewModel

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        //RecyclerView
        val adapter = ListePersonnelAdapter(
            ListePersonnelListener { personnelId ->
                Toast.makeText(context, "$personnelId", Toast.LENGTH_SHORT).show()
                viewModel.onSelectionPersonnel(personnelId.toLong())
            })

        binding.personnelListe.adapter = adapter
        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.personnelListe.layoutManager = manager

        viewModel.listePersonnelAAfficher.observe(
            viewLifecycleOwner,
            Observer { listePersonnel ->
                listePersonnel?.let {
                    adapter.submitList(it)
                    Timber.i("TEST Observation listePersonnelAAfficher")
                    adapter.notifyDataSetChanged()
                }
            })

        //Navigation
        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                CreationChantierViewModel.gestionNavigation.CONFIRMATION_ETAPE3 -> {
                    findNavController().navigate(R.id.action_creationChantier3Fragment_to_creationChantier4Fragment)
                    viewModel.onBoutonClicked()
                }
                CreationChantierViewModel.gestionNavigation.PASSAGE_ETAPE4 -> {
                    findNavController().navigate(R.id.action_creationChantier3Fragment_to_creationChantier4Fragment)
                    viewModel.onBoutonClicked()
                }
                CreationChantierViewModel.gestionNavigation.ANNULATION-> {
                    findNavController().navigate(R.id.action_creationChantier3Fragment_to_listeChantiersFragment)
                    viewModel.onBoutonClicked()
                }
            }

        })
        // Inflate the layout for this fragment
        return binding.root
    }


}
