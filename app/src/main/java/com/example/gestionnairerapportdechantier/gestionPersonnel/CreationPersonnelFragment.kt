package com.example.gestionnairerapportdechantier.gestionPersonnel


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentCreationPersonnelBinding

/**
 * A simple [Fragment] subclass.
 */
class CreationPersonnelFragment : Fragment() {

    val viewModel: GestionPersonnelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCreationPersonnelBinding.inflate(inflater, container, false)
        binding.executePendingBindings()

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creation_personnel, container, false)
    }


}
