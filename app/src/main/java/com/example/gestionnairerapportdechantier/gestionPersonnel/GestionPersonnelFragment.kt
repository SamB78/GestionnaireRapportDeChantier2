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
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentCreationPersonnelBinding


/**
 * A simple [Fragment] subclass.
 */
class GestionPersonnelFragment : Fragment() {

    val viewModel: GestionPersonnelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCreationPersonnelBinding.inflate(inflater, container, false)
        binding.executePendingBindings()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        viewModel.navigationPersonnel.observe(viewLifecycleOwner, Observer{ navigation ->
            when(navigation){
                GestionPersonnelViewModel.navigationMenuPersonnel.ENREGISTREMENT_PERSONNEL->{
                    Toast.makeText(activity, "Nouvelle entrée dans la BDD", Toast.LENGTH_SHORT)
                    viewModel.onBoutonClicked()
                    findNavController().navigate(R.id.action_creationPersonnelFragment_to_gestionPersonnelFragment)
                }
                GestionPersonnelViewModel.navigationMenuPersonnel.LISTE_PERSONNEL-> {
                    findNavController().navigate(R.id.action_creationPersonnelFragment_to_gestionPersonnelFragment)
                    viewModel.onBoutonClicked()
                }

            }
        })

            // Inflate the layout for this fragment
            return binding.root
}

}
