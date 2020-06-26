package com.example.gestionnairerapportdechantier.utils

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.entities.Personnel
import com.google.android.material.card.MaterialCardView
import timber.log.Timber

@BindingAdapter("clickedElement")
fun setColorIfClicked(view: MaterialCardView, item: Boolean) {
    view.isChecked = item
}


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {

//        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

    Glide.with(imgView.context)
        .load(imgUrl)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_business_black_24dp)
        )
//                .error(R.drawable.ic_broken_image))
        .into(imgView)
    Timber.e("TEST PASSAGE GLIDE")
}

@BindingAdapter("imageUrl2")
fun bindImage2(imgView: ImageView, imgUrl: String?) {
    imgView.visibility = View.GONE
    imgUrl?.let {
        imgView.visibility = View.VISIBLE
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_person_black_24dp)
            )
            .into(imgView)
        Timber.e("TEST PASSAGE GLIDE")
    }
}

@BindingAdapter("imageUrlItemViewPersonnel")
fun bindImageItemViewPersonnel(imgView: ImageView, imgUrl: String?) {

        imgView.visibility = View.VISIBLE
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_person_black_24dp)
            )
            .into(imgView)
        Timber.e("TEST PASSAGE GLIDE")
}


@BindingAdapter("isButtonDeleteVisible")
fun activateButtonDelete(imgButton: ImageButton, imgUrl: String?) {
    imgButton.visibility = View.GONE

    imgUrl?.let {
        imgButton.visibility = View.VISIBLE
    }
}


@BindingAdapter("isButtonAddPictureVisible")
fun activateButtonAddPicture(imgButton: Button, imgUrl: String?) {

    imgButton.visibility = View.VISIBLE

    imgUrl?.let {
        imgButton.visibility = View.GONE
    }
}


@BindingAdapter("personnelRole")
fun setTextDependingPersonnelRole(textView: TextView, personnel: Personnel){
    if(personnel.chefEquipe){
        textView.text = "Chef d'équipe"
    }
    else {
        if (personnel.interimaire)
        {
            textView.text = "Intérimaire"
        }
        else{
            textView.text = "Employé"
        }
    }
}