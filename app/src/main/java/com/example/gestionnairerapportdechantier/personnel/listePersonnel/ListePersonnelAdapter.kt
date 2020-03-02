package com.example.gestionnairerapportdechantier.personnel.listePersonnel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairerapportdechantier.databinding.PersonnelItemViewBinding
import com.example.gestionnairerapportdechantier.entities.Personnel

class ListePersonnelAdapter(val clickListener: ListePersonnelListener): ListAdapter<Personnel, ViewHolder>(
    ListePersonnelDiffCallBack()
){

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
class ListePersonnelDiffCallBack: DiffUtil.ItemCallback<Personnel>() {
    override fun areItemsTheSame(oldItem: Personnel, newItem: Personnel): Boolean {
        return oldItem.personnelId == newItem.personnelId
    }

    override fun areContentsTheSame(oldItem: Personnel, newItem: Personnel): Boolean {
        return oldItem == newItem
    }

}


// OK
class ViewHolder private constructor(val binding: PersonnelItemViewBinding): RecyclerView.ViewHolder(binding.root) {

    companion object{
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = PersonnelItemViewBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(
                binding
            )
        }
    }

    fun bind(
        item: Personnel,
        clickListener: ListePersonnelListener
    ){
        binding.clickListener = clickListener
        binding.personnel = item
        binding.nomPrenom.text = item.prenom + " " + item.nom
    }
}


//OK
class ListePersonnelListener(val clickListener: (personnelId: Int)-> Unit){
    fun onClick(personnel: Personnel) = clickListener(personnel.personnelId!!)
}
