package com.example.gestionnairerapportdechantier.rapportChantier.gestionRapportChantier.gestionPersonnel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairerapportdechantier.databinding.PersonnelNbHeuresTravailleesItemViewBinding
import com.example.gestionnairerapportdechantier.entities.Personnel

class GestionHeuresPersonnelAdapter(val clickListener: GestionHeuresPersonnelListener) :
    ListAdapter<Personnel, ViewHolder>(
        GestionHeuresPersonnelDiffCallBack()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }
}

class GestionHeuresPersonnelDiffCallBack : DiffUtil.ItemCallback<Personnel>() {
    override fun areItemsTheSame(oldItem: Personnel, newItem: Personnel): Boolean {
        return oldItem.nombreHeuresTravaillees == newItem.nombreHeuresTravaillees
    }

    override fun areContentsTheSame(oldItem: Personnel, newItem: Personnel): Boolean {
        return oldItem == newItem
    }
}

class ViewHolder private constructor(val binding: PersonnelNbHeuresTravailleesItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = PersonnelNbHeuresTravailleesItemViewBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(
                binding
            )
        }
    }

    fun bind(
        personnel: Personnel,
        clickListener: GestionHeuresPersonnelListener
    ) {
        binding.clickListener = clickListener
        binding.personnel = personnel
    }

}

class GestionHeuresPersonnelListener(val clickListener: (personnelId: Int)-> Unit){
    companion object valueClick {
       var click: Int = 0
    }
    fun onClickPlus(personnel: Personnel) {
        click = 1
        clickListener(personnel.personnelId!!)
    }
    fun onClickMoins(personnel: Personnel){
        click = -1
        clickListener(personnel.personnelId!!)
    }

}
