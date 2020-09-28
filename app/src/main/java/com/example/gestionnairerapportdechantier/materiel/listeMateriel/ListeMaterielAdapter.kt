package com.example.gestionnairerapportdechantier.materiel.listeMateriel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairerapportdechantier.databinding.MaterielItemViewBinding
import com.example.gestionnairerapportdechantier.entities.Materiel


class listeMaterielAdapter(val clickListener: ListeMaterielListener) :
    ListAdapter<Materiel, ViewHolder>(
        ListeMaterielDiffCallBack()
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

class ListeMaterielDiffCallBack : DiffUtil.ItemCallback<Materiel>() {
    override fun areItemsTheSame(oldItem: Materiel, newItem: Materiel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Materiel, newItem: Materiel): Boolean {
        return oldItem == newItem
    }

}

class ViewHolder private constructor(val binding: MaterielItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = MaterielItemViewBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(
                binding
            )
        }
    }

    fun bind(
        item: Materiel,
        clickListener: ListeMaterielListener
    ) {
        binding.clickListener = clickListener
        binding.materiel = item
    }
}


class ListeMaterielListener(val clickListener: (materielId: Int) -> Unit) {
    fun onClick(materiel: Materiel) = clickListener(materiel.id!!)

}