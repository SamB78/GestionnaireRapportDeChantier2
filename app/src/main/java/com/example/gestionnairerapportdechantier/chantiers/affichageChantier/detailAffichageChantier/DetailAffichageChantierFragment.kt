package com.example.gestionnairerapportdechantier.chantiers.affichageChantier.detailAffichageChantier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.AffichageChantierViewModel
import com.example.gestionnairerapportdechantier.databinding.FragmentDetailAffichageChantierBinding
import com.example.gestionnairerapportdechantier.personnel.listePersonnel.ListePersonnelAdapter
import com.example.gestionnairerapportdechantier.personnel.listePersonnel.ListePersonnelListener
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */

class DetailAffichageChantierFragment : Fragment() {

    val viewModel: AffichageChantierViewModel by navGraphViewModels(R.id.AffichageChantierNavGraph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDetailAffichageChantierBinding.inflate(inflater)
        binding.executePendingBindings()

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

        viewModel.listePersonnelChantierValide.observe(viewLifecycleOwner, Observer { listePersonnel ->
            listePersonnel?.let {
                adapter.submitList(it)
                Timber.i("valeurs listePersonnelChantier: $it")
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }

}
