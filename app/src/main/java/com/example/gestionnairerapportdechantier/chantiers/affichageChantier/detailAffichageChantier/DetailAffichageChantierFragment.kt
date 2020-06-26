package com.example.gestionnairerapportdechantier.chantiers.affichageChantier.detailAffichageChantier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.AffichageChantierViewModel
import com.example.gestionnairerapportdechantier.databinding.FragmentDetailAffichageChantierBinding

/**
 * A simple [Fragment] subclass.
 */

class DetailAffichageChantierFragment : Fragment() {

    val viewModel: AffichageChantierViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDetailAffichageChantierBinding.inflate(inflater)
        binding.executePendingBindings()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        // Inflate the layout for this fragment
        return binding.root
    }

}
