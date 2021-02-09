package com.example.gestionnairerapportdechantier.chantiers.creationChantier


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.CreationChantierNavGraphDirections
import com.example.gestionnairerapportdechantier.MainActivity
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentCreationChantier1Binding
import com.example.gestionnairerapportdechantier.utils.hideKeyboard
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CreationChantier1Fragment : Fragment() {

    private lateinit var viewModelFactory: CreationChantierViewModelFactory
    val viewModel: CreationChantierViewModel by navGraphViewModels(R.id.creationChantierNavGraph) { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
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
        }
        else -> {
            super.onOptionsItemSelected(item)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCreationChantier1Binding.inflate(inflater, container, false)
        binding.executePendingBindings()


        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSourceChantier = GestionnaireDatabase.getInstance(application).chantierDao
        val dataSourcePersonnel = GestionnaireDatabase.getInstance(application).personnelDao
        val dataSourceAssociationPersonnelChantier =
            GestionnaireDatabase.getInstance(application).associationPersonnelChantierDao
        val idChantier = CreationChantier1FragmentArgs.fromBundle(arguments!!).idChantier
        viewModelFactory = CreationChantierViewModelFactory(
            dataSourceChantier,
            dataSourcePersonnel,
            dataSourceAssociationPersonnelChantier,
            idChantier
        )

        //ViewModel
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        //Navigation
        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            hideKeyboard(activity as MainActivity)
            when (navigation) {
                CreationChantierViewModel.GestionNavigation.PASSAGE_ETAPE2 -> {
                    Toast.makeText(activity, "Nouvelle entrée dans la BDD", Toast.LENGTH_SHORT)
                    findNavController().navigate(R.id.action_creationChantier1Fragment_to_creationChantier2Fragment)
                    viewModel.onBoutonClicked()
                }
                CreationChantierViewModel.GestionNavigation.ANNULATION -> {
                    val action = CreationChantierNavGraphDirections.actionCreationChantierNavGraphPop()
                    findNavController().navigate(action)
                    viewModel.onBoutonClicked()
                }
            }
        })

        return binding.root
    }
}
