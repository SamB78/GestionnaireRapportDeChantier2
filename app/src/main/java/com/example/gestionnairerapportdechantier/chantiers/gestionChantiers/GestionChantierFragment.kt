package com.example.gestionnairerapportdechantier.chantiers.gestionChantiers


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gestionnairerapportdechantier.Database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionChantierBinding

/**
 * A simple [Fragment] subclass.
 */
class GestionChantierFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGestionChantierBinding.inflate(inflater, container, false)
        binding.executePendingBindings()

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = GestionnaireDatabase.getInstance(application).ChantierDao
        val viewModelFactory = GestionChantierViewModelFactory(dataSource, -1L)

        //ViewModel

        val viewModel: GestionChantierViewModel by viewModels() { viewModelFactory }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionChantierViewModel.gestionNavigation.ENREGISTREMENT_CHANTIER -> {
                    Toast.makeText(activity, "Nouvelle entrée dans la BDD", Toast.LENGTH_SHORT)
                    findNavController().navigate(R.id.action_gestionChantierFragment_to_listeChantiersFragment)
                    viewModel.onBoutonClicked()
                }
                GestionChantierViewModel.gestionNavigation.ANNULATION -> {
                    Toast.makeText(activity, "Saisie annulée", Toast.LENGTH_SHORT)
                    findNavController().navigate(R.id.action_gestionChantierFragment_to_listeChantiersFragment)
                    viewModel.onBoutonClicked()
                }
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }
}
