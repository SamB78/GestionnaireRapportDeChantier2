package com.example.gestionnairerapportdechantier.chantiers.affichageChantier

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gestionnairerapportdechantier.chantiers.affichageChantier.detailAffichageChantier.DetailAffichageChantierFragment
import com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.GestionRapportChantierFragment

class AffichageChantierViewerPagerAdapter(fragment: Fragment, private val fragments:ArrayList<Fragment>): FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment = fragments[position]

    override fun getItemCount(): Int {
        return fragments.size
    }
}