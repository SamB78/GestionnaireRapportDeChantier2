package com.example.gestionnairerapportdechantier.rapportChantier.listeRapportsChantier

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairerapportdechantier.databinding.RapportChantierItemViewBinding
import com.example.gestionnairerapportdechantier.entities.RapportChantier

class ListeRapportChantiersAdapter(val clickListener: ListeRapportChantiersListener) :
    ListAdapter<RapportChantier, ViewHolder>(
        ListeRapportChantiersDiffCallBack()
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

class ListeRapportChantiersDiffCallBack : DiffUtil.ItemCallback<RapportChantier>() {
    override fun areItemsTheSame(oldItem: RapportChantier, newItem: RapportChantier): Boolean {
        return oldItem.rapportChantierId == newItem.rapportChantierId
    }

    override fun areContentsTheSame(oldItem: RapportChantier, newItem: RapportChantier): Boolean {
        return oldItem == newItem
    }

}

class ViewHolder private constructor(val binding: RapportChantierItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RapportChantierItemViewBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(
                binding
            )
        }
    }

    fun bind(
        item: RapportChantier,
        clickListener: ListeRapportChantiersListener
    ) {
//        binding.clickListener = clickListener
        binding.item = item
    }
}

class ListeRapportChantiersListener(val clickListener: (rapportChantierId: Int) -> Unit) {
    fun onClick( rapportChantier: RapportChantier) = clickListener(rapportChantier.rapportChantierId!!)

}
