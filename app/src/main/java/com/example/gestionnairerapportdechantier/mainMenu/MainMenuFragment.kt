package com.example.gestionnairerapportdechantier.mainMenu


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentMainMenuBinding

/**
 * A simple [Fragment] subclass.
 */
class MainMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        binding.executePendingBindings()

        binding.lifecycleOwner = this

        val viewModel = ViewModelProvider(this).get(MainMenuViewModel::class.java)

        binding.viewModel = viewModel


        val navController = findNavController()

        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            when (it) {
                MainMenuViewModel.navigationMainMenu.PASSAGE_PAGE_PERSONNEL -> {
                    navController.navigate(R.id.action_mainMenuFragment_to_listePersonnelFragment)
                    viewModel.onBoutonClicked()
                }
                MainMenuViewModel.navigationMainMenu.PASSAGE_PAGE_CHANTIERS -> {
                    navController.navigate(R.id.action_mainMenuFragment_to_listeChantiersFragment)
                    viewModel.onBoutonClicked()
                }
                MainMenuViewModel.navigationMainMenu.PASSAGE_PAGE_RAPPORTS_CHANTIER -> {
//                    navController.navigate(R.id.action_mainMenuFragment_to_listeRapportsChantierFragment)
                    viewModel.onBoutonClicked()
                }
                MainMenuViewModel.navigationMainMenu.PASSAGE_PAGE_VEHICULES -> {
                    navController.navigate(R.id.action_mainMenuFragment_to_listeVehiculesFragment)
                    viewModel.onBoutonClicked()
                }
                MainMenuViewModel.navigationMainMenu.PASSAGE_PAGE_MATERIEL -> {
                    navController.navigate(R.id.action_mainMenuFragment_to_listeMaterielFragment)
                    viewModel.onBoutonClicked()
                }
            }

        })

        // Inflate the layout for this fragment
        return binding.root
    }


}
