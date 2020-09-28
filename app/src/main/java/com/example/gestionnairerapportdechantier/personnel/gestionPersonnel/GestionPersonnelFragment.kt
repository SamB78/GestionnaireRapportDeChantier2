package com.example.gestionnairerapportdechantier.personnel.gestionPersonnel


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase

import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentGestionPersonnelBinding
import com.theartofdev.edmodo.cropper.CropImage
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class GestionPersonnelFragment : Fragment() {

    private val PERMISSION_CODE = 1000

    lateinit var viewModelFactory: GestionPersonnelViewModelFactory
    //ViewModel
    val viewModel: GestionPersonnelViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGestionPersonnelBinding.inflate(inflater, container, false)
        binding.executePendingBindings()

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = GestionnaireDatabase.getInstance(application).personnelDao
        val idPersonnel = GestionPersonnelFragmentArgs.fromBundle(arguments!!).idPersonnel
        viewModelFactory = GestionPersonnelViewModelFactory(dataSource, idPersonnel)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                GestionPersonnelViewModel.gestionNavigation.ENREGISTREMENT_PERSONNEL -> {
                    Toast.makeText(activity, "Nouvelle entrée dans la BDD", Toast.LENGTH_SHORT)
                    viewModel.onBoutonClicked()
                    findNavController().navigate(R.id.action_gestionPersonnelFragment_to_listePersonnelFragment)
                }
                GestionPersonnelViewModel.gestionNavigation.ANNULATION -> {
                    Toast.makeText(activity, "Saisie annulée", Toast.LENGTH_SHORT)
                    findNavController().navigate(R.id.action_gestionPersonnelFragment_to_listePersonnelFragment)
                    viewModel.onBoutonClicked()
                }
                GestionPersonnelViewModel.gestionNavigation.AJOUT_PHOTO->{
                    selectImage()
                    viewModel.onBoutonClicked()
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
                    .setAspectRatio(1,1)
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
