package com.example.gestionnairerapportdechantier.vehicules.listeVehicules

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairerapportdechantier.databinding.VehiculeItemViewBinding
import com.example.gestionnairerapportdechantier.entities.Vehicule


class listeVehiculesAdapter(val clickListener: ListeVehiculeListener) :
    ListAdapter<Vehicule, ViewHolder>(
        ListeVehiculesDiffCallBack()
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

class ListeVehiculesDiffCallBack : DiffUtil.ItemCallback<Vehicule>() {
    override fun areItemsTheSame(oldItem: Vehicule, newItem: Vehicule): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Vehicule, newItem: Vehicule): Boolean {
        return oldItem == newItem
    }

}

class ViewHolder private constructor(val binding: VehiculeItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = VehiculeItemViewBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(
                binding
            )
        }
    }

    fun bind(
        item: Vehicule,
        clickListener: ListeVehiculeListener
    ) {
        binding.clickListener = clickListener
        binding.vehicule = item
    }
}


class ListeVehiculeListener(val clickListener: (vehiculeId: Int) -> Unit) {
    fun onClick(vehicule: Vehicule) = clickListener(vehicule.id!!)

}