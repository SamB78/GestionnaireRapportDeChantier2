package com.example.gestionnairerapportdechantier.chantiers.listeChantiers


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairerapportdechantier.databinding.ChantierItemViewBinding
import com.example.gestionnairerapportdechantier.entities.Chantier
import com.example.gestionnairerapportdechantier.entities.Personnel

class ListeChantierAdapter(val clickListener: ListeChantierListener) :
    ListAdapter<Chantier, ViewHolder>(
        ListeChantierDiffCallBack()
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


// OK
class ListeChantierDiffCallBack : DiffUtil.ItemCallback<Chantier>() {
    override fun areItemsTheSame(oldItem: Chantier, newItem: Chantier): Boolean {
        return oldItem.chantierId == newItem.chantierId
    }

    override fun areContentsTheSame(oldItem: Chantier, newItem: Chantier): Boolean {
        return oldItem == newItem
    }

}


// OK
class ViewHolder private constructor(val binding: ChantierItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ChantierItemViewBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(
                binding
            )
        }
    }

    fun bind(
        item: Chantier,
        clickListener: ListeChantierListener
    ) {
        binding.clickListener = clickListener
        binding.chantier = item
        binding.nomChantier.text = item.nomChantier
        binding.numeroChantier.text = item.numeroChantier.toString()
    }
}


//OK
class ListeChantierListener(val clickListener: (chantierId: Int) -> Unit) {
    fun onClick(chantier: Chantier) = clickListener(chantier.chantierId!!)
}
