package com.example.gestionnairerapportdechantier.chantiers.creationChantier


import android.Manifest
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.MainActivity
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.databinding.FragmentCreationChantier4Binding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.theartofdev.edmodo.cropper.CropImage
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CreationChantier4Fragment : Fragment() {

    private val PERMISSION_CODE = 1000
    //ViewModel
    val viewModel: CreationChantierViewModel by navGraphViewModels(R.id.creationChantierNavGraph)

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite->{
            MaterialAlertDialogBuilder(context)
                .setTitle("Annulation")
                .setMessage("Souhaitez vous annuler la création du nouveau chantier ?")
                .setNegativeButton("QUITTER") { dialog, which ->
                    // Respond to negative button press
                    viewModel.onClickButtonAnnuler()
                }
                .setPositiveButton("CONTINUER") { dialog, which ->
                    // Respond to positive button press
                    dialog.dismiss()
                }
                .show()
            true
        }else-> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCreationChantier4Binding.inflate(inflater, container, false)
        binding.executePendingBindings()


        //ViewModel Binding
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        viewModel.navigation.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                CreationChantierViewModel.gestionNavigation.AJOUT_IMAGE -> {
                    selectImage()
                    viewModel.onBoutonClicked()
                }
                CreationChantierViewModel.gestionNavigation.PASSAGE_ETAPE_RESUME -> {
                    findNavController().navigate(R.id.action_creationChantier4Fragment_to_resumeCreationSignalementFragment)
                    viewModel.onBoutonClicked()
                }
                CreationChantierViewModel.gestionNavigation.ANNULATION -> {
                    findNavController().navigate(R.id.action_creationChantier4Fragment_to_listeChantiersFragment)
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
            if (checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(
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



//    private fun selectImage() {
//        //imageView.setImageResource(0)
//        val items = arrayOf<CharSequence>(
//            "Take Photo", "Choose from Library",
//            "Cancel"
//        )
//        val builder = MaterialAlertDialogBuilder(context)
//        builder.setTitle("Add Photo!")
//        builder.setIcon(R.mipmap.ic_launcher)
//        builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
//            if (items[item] == "Take Photo") {
//                //if system os is Marshmallow or Above, we need to request runtime permission
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
//                        == PackageManager.PERMISSION_DENIED ||
//                        checkSelfPermission(
//                            requireContext(),
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        )
//                        == PackageManager.PERMISSION_DENIED
//                    ) {
//                        //permission was not enabled
//                        val permission = arrayOf(
//                            Manifest.permission.CAMERA,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        )
//                        //show popup to request permission
//                        requestPermissions(permission, PERMISSION_CODE)
//                    } else {
//                        //permission already granted
//                        dispatchTakePictureIntent()
//                    }
//                } else {
//                    //system os is < marshmallow
//                    dispatchTakePictureIntent()
//                }
//
//            } else if (items[item] == "Choose from Library") {
//
//                // for fragment (DO NOT use `getActivity()`)
//                CropImage.activity()
//                    .start(getContext()!!, this);
//
//            } else if (items[item] == "Cancel") {
//                dialog.dismiss()
//            }
//        })
//        builder.show()
//    }
//
//    val REQUEST_TAKE_PHOTO = 1
//
//    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            // Ensure that there's a camera activity to handle the intent
//            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
//                // Create the File where the photo should go
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    // Error occurred while creating the File
//                    null
//                }
//                // Continue only if the File was successfully created
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        context!!,
//                        "com.example.gestionnairerapportdechantier.fileprovider",
//                        it
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
//                }
//            }
//        }
//    }
//
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        //called when user presses ALLOW or DENY from Permission Request Popup
//        when (requestCode) {
//            PERMISSION_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] ==
//                    PackageManager.PERMISSION_GRANTED
//                ) {
//                    //permission from popup was granted
//                    dispatchTakePictureIntent()
//                } else {
//                    //permission from popup was denied
//                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        //called when image was captured from camera intent
//        if (resultCode == Activity.RESULT_OK) {
//            //set image captured to image view
//            Toast.makeText(context, "Photo enregistrée", Toast.LENGTH_SHORT).show()
//            viewModel.ajoutPathImage(currentPhotoPath)
//
//        }
//        else {
//            val fileToDelete = File(currentPhotoPath)
//            if (fileToDelete.exists()) {
//                if (fileToDelete.delete()) {
//                    if (fileToDelete.exists()) {
//                        fileToDelete.canonicalFile.delete()
//                        if (fileToDelete.exists()) {
//                            fileToDelete.delete()
//                        }
//                    }
//                    Timber.i("File Deleted %s", currentPhotoPath)
//                } else {
//                    Timber.i( "File not Deleted %s", currentPhotoPath)
//                }
//            }
//        }
//    }
//
//    lateinit var currentPhotoPath: String
//
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
//        return File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        ).apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            currentPhotoPath = absolutePath
//            Timber.i("path: $currentPhotoPath")
//        }
//    }




