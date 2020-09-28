package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionPersonnel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionRapportChantierPersonnelBinding
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierViewModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


class GestionRapportChantierPersonnelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =
            FragmentGestionRapportChantierPersonnelBinding.inflate(inflater, container, false)
        binding.executePendingBindings()

        //ViewModel
        val viewModel: GestionRapportChantierViewModel by navGraphViewModels(R.id.gestionRapportChantiers)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //RecyclerView
        val adapter = GestionHeuresPersonnelAdapter(
            GestionHeuresPersonnelListener { personnelId ->
                when (GestionHeuresPersonnelListener.click) {
                    -1 -> {
                        val onClickResult = viewModel.onClickMoinsHorairesTravailles(personnelId)
                        if(!onClickResult){
                            Snackbar.make(view!!, "Vous êtes déjà à 0 heures travaillées", Snackbar.LENGTH_SHORT).show()
                        }

                    }
                    +1 -> {
                        val onClickResult  = viewModel.onClickPlusHorairesTravailles(personnelId)
                        if(!onClickResult){
                            Snackbar.make(view!!, " Vous ne pouvez pas travailler plus de 8 heures !", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    else ->{
                        Toast.makeText(context, "$personnelId +0", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
        binding.personnelListe.adapter = adapter

        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.personnelListe.layoutManager = manager

        // Affichage liste Personnel dans RecyclerView
        viewModel.listePersonnelChantierValide.observe(
            viewLifecycleOwner,
            Observer { listePersonnel ->
                listePersonnel?.let { it ->
                    Timber.i("Mise à jour de la liste")
                    adapter.submitList(it)
                    adapter.notifyDataSetChanged()
                    listePersonnel.forEach {
                        Timber.i("liste personnel: $it")
                    }
                }
            })


        return binding.root
    }

}