package com.example.gestionnairerapportdechantier.gestionPersonnel


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gestionnairerapportdechantier.Database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionPersonnelBinding
import com.example.gestionnairerapportdechantier.mainMenu.MainMenuViewModel

/**
 * A simple [Fragment] subclass.
 */
class GestionPersonnelFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGestionPersonnelBinding.inflate(inflater,container,false)
        binding.executePendingBindings()

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = GestionnaireDatabase.getInstance(application).PersonnelDao
        val viewModelFactory = GestionPersonnelViewModelFactory(dataSource)

        //ViewModel
        val viewModel: GestionPersonnelViewModel by activityViewModels(){viewModelFactory}

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.navigationPersonnel.observe(viewLifecycleOwner, Observer { navigation ->

            when(navigation){
              GestionPersonnelViewModel.navigationMenuPersonnel.CREATION_PERSONNEL ->{
                  Toast.makeText(activity, "Passage creation Signalement", Toast.LENGTH_SHORT).show()
                  findNavController().navigate(R.id.action_gestionPersonnelFragment_to_creationPersonnelFragment)
                  viewModel.onBoutonClicked()
              }
        }
    })

        // Inflate the layout for this fragment
        return binding.root
    }


}
