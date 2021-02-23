package com.example.gestionnairerapportdechantier.chantiers.creationChantier


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gestionnairerapportdechantier.CreationChantierNavGraphDirections
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentCreationChantier2Binding
import com.example.gestionnairerapportdechantier.databinding.PersonnelItemViewBinding
import com.example.gestionnairerapportdechantier.personnel.listePersonnel.ListePersonnelAdapter
import com.example.gestionnairerapportdechantier.personnel.listePersonnel.ListePersonnelListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class creationChantier2Fragment : Fragment() {

    val viewModel: CreationChantierViewModel by navGraphViewModels(R.id.creationChantierNavGraph)

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    //Menu Option
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            MaterialAlertDialogBuilder(context!!)
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
        }
        else -> {
            super.onOptionsItemSelected(item)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCreationChantier2Binding.inflate(inflater, container, false)
        binding.executePendingBindings()

        //ViewModel
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        //DialogConfirmation
        val confirmationDialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Vous avez sélectionné:")
            .setNegativeButton("Annuler") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Valider") { dialog, which ->
                viewModel.onClickChefChantierValide()
                dialog.dismiss()
            }

        val customLayout = layoutInflater.inflate(R.layout.personnel_item_view, null)



//        //Remplissage XML de la dialog de confirmation
//        viewModel.chefChantierSelectionne.observe(viewLifecycleOwner, Observer {
//
//            val bindingDialog = PersonnelItemViewBinding.inflate(inflater, customLayout as ViewGroup, false)
//
//            bindingDialog.personnel = it
//            customLayout.nomPrenom.text = it.prenom + " " + it.nom
//            bindingDialog.numero.text = it.numContact
//            if (it.chefEquipe) {
//                customLayout.role.text = "Chef d'équipe"
//            }
////            confirmationDialog.setView(customLayout)
//        })


        //RecyclerView
        val adapter = ListePersonnelAdapter(
            ListePersonnelListener { personnelId ->
                Toast.makeText(context, "$personnelId", Toast.LENGTH_LONG).show()

                viewModel.onClickChefChantier(personnelId.toLong())

                if (customLayout.parent != null)
                    (customLayout.parent as ViewGroup).removeView(customLayout) // <- fix
                confirmationDialog.setView(customLayout)
                val bindingDialog = PersonnelItemViewBinding.inflate(inflater, customLayout as ViewGroup, false)
                bindingDialog.personnel = viewModel.chefChantierSelectionne.value
                confirmationDialog.setView(bindingDialog.root)
                confirmationDialog.show()
            })

        binding.RecyclerViewChefEquipe.adapter = adapter

        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.RecyclerViewChefEquipe.layoutManager = manager

        viewModel.listeChefsDeChantier.observe(viewLifecycleOwner, Observer { listeChefsChantier ->
            listeChefsChantier?.let {
                adapter.submitList(it)
                Timber.i("valeurs listChefsDeChantier: $it")
            }
        })

        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                CreationChantierViewModel.GestionNavigation.PASSAGE_ETAPE3 -> {
                    findNavController().navigate(R.id.action_creationChantier2Fragment_to_creationChantier3Fragment)
                    viewModel.onBoutonClicked()
                }
                CreationChantierViewModel.GestionNavigation.ANNULATION -> {
                    val action = CreationChantierNavGraphDirections.actionCreationChantierNavGraphPop()
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }

            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

}


