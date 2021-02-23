package com.example.gestionnairerapportdechantier.materiel.gestionMateriel

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionMaterielBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.theartofdev.edmodo.cropper.CropImage
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class GestionMaterielFragment : Fragment() {

    private val PERMISSION_CODE = 1000

    lateinit var viewModelFactory: GestionMaterielViewModelFactory

    //ViewModel
    val viewModel: GestionMaterielViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = GestionnaireDatabase.getInstance(application).materielDao
        val idMateriel = GestionMaterielFragmentArgs.fromBundle(arguments!!).idMateriel
        viewModelFactory = GestionMaterielViewModelFactory(dataSource, idMateriel)

        val binding = FragmentGestionMaterielBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()




        // Calendrier
        var date = ""
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd =
            context?.let {
                DatePickerDialog(it, DatePickerDialog.OnDateSetListener { view, year, month, day ->

                    // Display Selected date in textbox
                    Timber.i("DATE:  $day  $month $year")
                    val localDate = LocalDate.of(year, month, day)
                    date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    Timber.i("Date pre VM = $date")
//                    viewModel.onDateSelected(localDate)
                    binding.invalidateAll()

                }, year, month, day)
            }
        dpd?.datePicker?.maxDate = System.currentTimeMillis()


        // Create the date picker builder and set the title
        val builder = MaterialDatePicker.Builder.datePicker()
        // create the date picker
        val datePicker = builder.build()
        var date2: Long = 0
        // set listener when date is selected
        datePicker.addOnPositiveButtonClickListener {


            viewModel.onDateSelected(Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate())
        }

        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionMaterielViewModel.gestionNavigation.ENREGISTREMENT_MATERIEL -> {
                    viewModel.onBoutonClicked()
                    findNavController().navigate(R.id.action_gestionMaterielFragment_to_listeMaterielFragment)
                }
                GestionMaterielViewModel.gestionNavigation.ANNULATION -> {
                    viewModel.onBoutonClicked()
                    findNavController().navigate(R.id.action_gestionMaterielFragment_to_listeMaterielFragment)
                }
                GestionMaterielViewModel.gestionNavigation.AJOUT_PHOTO -> {
                    selectImage()
                    viewModel.onBoutonClicked()
                }
                GestionMaterielViewModel.gestionNavigation.CHOIX_DATE_IMMAT -> {
                    viewModel.onBoutonClicked()
                    datePicker.show(activity?.supportFragmentManager!!, "MyTAG")
                }
            }

        })
        // Inflate the layout for this fragment
        return binding.root

    }

    private fun selectImage() {

        //if system os is Marshmallow or Above, we need to request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                //permission was not enabled
                val permission = arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                //show popup to request permission
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                //permission already granted
                CropImage.activity()
                    .setAspectRatio(1, 1)
                    .start(context!!, this);
            }
        } else {
            //system os is < marshmallow
            CropImage.activity()
                .start(context!!, this);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //called when image was captured from camera intent
        when (requestCode) {

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    result.uri?.let { uri ->
                        val cachePhotoFile = File(uri.path)
                        saveCroppedImage(cachePhotoFile)
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Timber.e("Crop error: ${result.error}")
                }
            }
        }
    }


    private fun saveCroppedImage(file: File) {
        Timber.i("path: $file")
        // val cachePhotoFile = File(uri.toString())
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            null
        }

        file.copyTo(photoFile!!, true)// This method will be executed once the timer is over
        viewModel.ajoutPathImage(photoFile.absolutePath.toString())
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            Timber.i("path: $currentPhotoPath")
        }
    }

}

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(
            activity,
            this,
            hour,
            minute,
            DateFormat.is24HourFormat(activity)
        )
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
    }

}