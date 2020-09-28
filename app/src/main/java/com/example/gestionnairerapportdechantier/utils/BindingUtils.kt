package com.example.gestionnairerapportdechantier.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.entities.Personnel
import com.google.android.material.card.MaterialCardView
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
fun setTextDependingPersonnelRole(textView: TextView, personnel: Personnel) {
    if (personnel.chefEquipe) {
        textView.text = "Chef d'équipe"
    } else {
        if (personnel.interimaire) {
            textView.text = "Intérimaire"
        } else {
            textView.text = "Employé"
        }
    }
}


@BindingAdapter("showDate")
fun setTextFromDate(textView: TextView, date: LocalDate?) {
    if (date != null) {
        textView.text = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    }
}

@BindingAdapter("valueToCheckForMaxButton", "valueMax")
fun disableButtonMax( imageButton: ImageButton, value: Int, valueMax: Int) {
    if (value >= valueMax) {
        imageButton.isEnabled = false
        imageButton.isClickable = false
    } else {
        imageButton.isEnabled = true
        imageButton.isClickable = true
    }
}

@BindingAdapter("valueToCheckForMinButton")
fun disableButtonMin(imageButton: ImageButton, value: Int) {
    if (value <= 0) {
        imageButton.isEnabled = false
        imageButton.isClickable = false
    } else {
        imageButton.isEnabled = true
        imageButton.isClickable = true
    }
}


// PLUS TARD


@BindingAdapter("textRespectingMinAndMax")
fun setTextRespectingMinAndMax(editText: EditText, value: Int) {

}


@BindingAdapter("input")
fun bindIntegerInText(
    editText: EditText,
    value: String
) {
    editText.setText(value)
    // Set the cursor to the end of the text
    editText.setSelection(editText.text!!.length)
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
            //inverseBindingListener.onChange();
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            Timber.i("TEXT CHANGED")


        }

        override fun afterTextChanged(s: Editable) {
            //inverseBindingListener.onChange();
            Timber.i("AFTER TEXT CHANGED")
        }
    })
}


@InverseBindingAdapter(attribute = "app:input", event = "app:inputAttrChanged")
fun bindCountryInverseAdapter(view: AppCompatEditText): Int {
    val string = view.text.toString()
    return if (string.isEmpty()) 0 else string.toInt()
}