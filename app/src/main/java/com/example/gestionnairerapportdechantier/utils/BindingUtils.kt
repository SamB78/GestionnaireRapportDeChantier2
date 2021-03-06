package com.example.gestionnairerapportdechantier.utils

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.entities.Personnel
import com.example.gestionnairerapportdechantier.rapportChantier.affichageRapportChantier.AffichageDetailsRapportChantierViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt


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
    Timber.i("TEST PASSAGE GLIDE")
}

@BindingAdapter("imageUrl2")
fun bindImage2(imgView: ImageView, imgUrl: String?) {
    imgView.visibility = View.INVISIBLE
    imgUrl?.let {
        imgView.visibility = View.VISIBLE
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_person_black_24dp)
            )
            .into(imgView)
        Timber.i("TEST PASSAGE GLIDE")
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
    Timber.i("TEST PASSAGE GLIDE")
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

@BindingAdapter("setCardVisibility")
fun setCardVisibility(cardView: MaterialCardView, value: Int) {
    when (value) {
        1 -> {
            Timber.i(" button.visibility = View.VISIBLE")
            cardView.visibility = View.VISIBLE
        }
        2 -> {
            Timber.i("  button.visibility = View.GONE")
            cardView.visibility = View.GONE

        }
    }
}


@BindingAdapter("setLayoutVisibility")
fun setLayoutVisibility(linearLayout: LinearLayout, value: Int) {
    when (value) {
        2 -> {
            Timber.i(" button.visibility = View.VISIBLE")
            linearLayout.visibility = View.VISIBLE
        }
        1 -> {
            Timber.i("  button.visibility = View.GONE")
            linearLayout.visibility = View.GONE

        }
    }
}

@BindingAdapter("setLayoutVisibility")
fun setLayoutVisibility(layout: ConstraintLayout, boolean: Boolean) {
    when (boolean) {
        true -> {
            Timber.i(" button.visibility = View.VISIBLE")
            layout.visibility = View.VISIBLE
        }
        false -> {
            Timber.i("  button.visibility = View.GONE")
            layout.visibility = View.GONE

        }
    }
}

@BindingAdapter("setTextFromFunction")
fun setTextFromFunction(textView: TextView, text: String) {
    textView.text = text
}


@BindingAdapter("showDate")
fun setTextFromDate(textView: TextView, date: Date?) {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    if (date != null) {
        calendar.time = date
        textView.text = calendar.time.toString()
    }
}

@BindingAdapter("valueToCheckForMaxButton", "valueMax")
fun disableButtonMax(imageButton: ImageButton, value: Int, valueMax: Int) {
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


@BindingAdapter("booleanSaved", "dataEdited")
fun setBackgroundColor(cardView: MaterialCardView, booleanSaved: Boolean, dataEdited: Boolean) {
    val vert: Int = R.color.colorDataSavedTrue
    val rouge: Int = R.color.colorDataSavedFalse
    val orange: Int = R.color.colorDataEdited

    if (booleanSaved) {
        if (dataEdited) cardView.setCardBackgroundColor(
            ContextCompat.getColor(
                cardView.context,
                orange
            )
        )
        else cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, vert))
    } else cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, rouge))
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

@BindingAdapter("scrollTo")
fun scrollTo(scrollView: ScrollView, boolean: Boolean) {
    Timber.i("Entree scrollTo")
    if (boolean) {
        Timber.i("scrollTo = True")
        scrollView.scrollTo(0, R.id.editTextTextMultiLine)
    } else {
        Timber.i("scrollTo = False")
    }
}


@BindingAdapter("date")
fun convertDateToTexView(textView: TextView, date: LocalDate?) {

    val sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    textView.text = date?.let { sdf.format(date) }
}

@BindingAdapter("date")
fun convertDateToEditTextField(textInputEditText: TextInputEditText, date: Date?) {

    if (date != null) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = date
        textInputEditText.setText(
            "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
                calendar.get(
                    Calendar.YEAR
                )
            } "
        )
    }
}

@BindingAdapter("layout_max_width")
fun setLayoutWidth(
    constraintLayout: ConstraintLayout,
    value: Int
) {
    val value = 180 + (value * 65)
    Timber.i("value width = $value")

    constraintLayout.maxWidth = dpToPx(value)

}

fun dpToPx(dp: Int): Int {
    val metrics = Resources.getSystem().displayMetrics.density
    return (dp.toFloat() * metrics).roundToInt()
}

@BindingAdapter("setGridLayout")
fun setGridLayout(recyclerView: RecyclerView, value: Int){
    val manager = GridLayoutManager(recyclerView.context, value, GridLayoutManager.VERTICAL, false)
    recyclerView.layoutManager = manager
}


@BindingAdapter("typeChantier")
fun setTextTypeChantier(textView: TextView, value: Int){
    if(value == 1) textView.text = "CHANTIER"
    else textView.text = "ENTRETIEN"
}


@BindingAdapter("adapter")
fun setAdapter(autoCompleteTextView: AutoCompleteTextView, items: List<String>){
    val adapter = ArrayAdapter(autoCompleteTextView.context, R.layout.list_item, items)
    autoCompleteTextView.setAdapter(adapter)
}